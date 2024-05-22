package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.community.dto.request.ModifyCommentRequestDTO;
import com.meowzip.apiserver.community.dto.request.WriteCommentRequestDTO;
import com.meowzip.apiserver.community.dto.response.CommentResponseDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.notification.service.NotificationSendService;
import com.meowzip.community.entity.CommunityBlockMember;
import com.meowzip.community.entity.CommunityComment;
import com.meowzip.community.entity.CommunityPost;
import com.meowzip.community.entity.TargetType;
import com.meowzip.community.repository.CommunityCommentRepository;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityCommentService {

    private final CommunityPostService postService;
    private final CommunityCommentRepository commentRepository;
    private final CommunityBlockMemberService blockMemberService;
    private final CommunityReportService reportService;
    private final NotificationSendService notificationSendService;

    @Transactional
    public void write(Long postId, Member member, WriteCommentRequestDTO requestDTO) {
        CommunityPost post = postService.getPostById(postId);

        CommunityComment parentComment = null;
        if (requestDTO.parentCommentId() != null) {
            parentComment = commentRepository.findById(requestDTO.parentCommentId())
                    .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.COMMENT_NOT_FOUND));
        }

        CommunityComment comment = requestDTO.toComment(post, member, parentComment);
        commentRepository.save(comment);

        if (!post.getMember().equals(member)) {
            String link = "/community/posts/" + postId;

            // TODO 프론트 분들께 이동 링크 요청
            notificationSendService.send(post.getMember(), NotificationCode.MN001, link, member.getNickname(), requestDTO.parentCommentId() == null ? "댓글" : "답글");
        }
    }

    public List<CommentResponseDTO> showComments(Long postId, Member member) {
        CommunityPost post = postService.getPostById(postId);

        var comments = post.getComments();
        var blockMembers = blockMemberService.getByMember(member);
        var filtered = filterComments(comments, blockMembers);

        return changeCommentArchitect(filtered, member);
    }

    private List<CommentResponseDTO> changeCommentArchitect(List<CommunityComment> comments, Member member) {
        List<CommentResponseDTO> commentResponses = new LinkedList<>();

        comments.forEach(comment -> {
            CommentResponseDTO commentResponse = new CommentResponseDTO(comment, member);

            if (!comment.isReply()) {
                commentResponses.add(commentResponse);
            }
        });

        return commentResponses;
    }

    private List<CommunityComment> filterComments(List<CommunityComment> comments, List<CommunityBlockMember> blockMembers) {
        List<CommunityComment> filtered = new LinkedList<>();

        for (var comment : comments) {
            if (isBlocked(comment, blockMembers)) {
                continue;
            }

            var filteredReplies = filterComments(comment.getReplies(), blockMembers);
            comment.setReplies(filteredReplies);

            filtered.add(comment);
        }

        return filtered;
    }

    private boolean isBlocked(CommunityComment comment, List<CommunityBlockMember> blockMembers) {
        return blockMembers.stream()
                .anyMatch(blockMember -> comment.isBlocked(blockMember.getBlockedMember()));
    }

    private CommunityComment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional
    public void modify(Long postId, Long commentId, Member member, ModifyCommentRequestDTO requestDTO) {
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.COMMENT_NOT_FOUND));

        if (!isWriter(member, comment)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        comment.modify(requestDTO.content());
    }

    @Transactional
    public void delete(Long postId, Long commentId, Member member) {
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.COMMENT_NOT_FOUND));

        if (!isWriter(member, comment)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        commentRepository.delete(comment);
    }

    private boolean isWriter(Member member, CommunityComment comment) {
        return member.getId().equals(comment.getMember().getId());
    }

    @Transactional
    public void blockWriter(Long commentId, Member member) {
        CommunityComment comment = getCommentById(commentId);

        if (isWriter(member, comment)) {
            throw new ClientException.BadRequest(EnumErrorCode.BAD_REQUEST);
        }

        blockMemberService.block(member, comment.getMember());
    }

    @Transactional
    public void report(Long commentId, Member member) {
        CommunityComment comment = getCommentById(commentId);

        if (isWriter(member, comment)) {
            throw new ClientException.BadRequest(EnumErrorCode.BAD_REQUEST);
        }

        reportService.report(member, TargetType.COMMENT, comment.getId());
    }
}
