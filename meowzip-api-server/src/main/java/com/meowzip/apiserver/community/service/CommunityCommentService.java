package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.community.dto.request.ModifyCommentRequestDTO;
import com.meowzip.apiserver.community.dto.request.WriteCommentRequestDTO;
import com.meowzip.apiserver.community.dto.response.CommentResponseDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.community.entity.CommunityComment;
import com.meowzip.community.entity.CommunityPost;
import com.meowzip.community.repository.CommunityCommentRepository;
import com.meowzip.member.entity.Member;
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
    }

    public List<CommentResponseDTO> showComments(Long postId, Member member) {
        CommunityPost post = postService.getPostById(postId);

        return changeCommentArchitect(post.getComments(), member);
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
}
