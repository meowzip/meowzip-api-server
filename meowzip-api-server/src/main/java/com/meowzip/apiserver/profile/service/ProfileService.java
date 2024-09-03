package com.meowzip.apiserver.profile.service;

import com.meowzip.apiserver.cat.service.CatService;
import com.meowzip.apiserver.community.dto.response.PostResponseDTO;
import com.meowzip.apiserver.community.service.CommunityPostService;
import com.meowzip.apiserver.notification.service.NotificationService;
import com.meowzip.apiserver.profile.dto.response.MyProfileInfoResponseDTO;
import com.meowzip.apiserver.profile.dto.response.ProfileInfoResponseDTO;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final NotificationService notificationService;
    private final CatService catService;
    private final CommunityPostService communityPostService;

    public MyProfileInfoResponseDTO getMyProfileInfo(Member member) {
        return MyProfileInfoResponseDTO.builder()
                .profileImageUrl(member.getProfileImage())
                .nickname(member.getNickname())
                .existsNewNotification(notificationService.isExistsUnreadNotification(member))
                .catCount(catService.countCats(member))
                .postCount(communityPostService.countPosts(member))
                .bookmarkCount(communityPostService.countBookmarks(member))
                .build();
    }

    public ProfileInfoResponseDTO getProfileInfo(Member member) {
        return ProfileInfoResponseDTO.builder()
                .profileImageUrl(member.getProfileImage())
                .nickname(member.getNickname())
                .catCount(catService.countCats(member))
                .postCount(communityPostService.countPosts(member))
                .build();
    }

    public List<PostResponseDTO> showPostsByWriter(Member loggedInMember, Member writer, PageRequest pageRequest) {
        return communityPostService.showPostsByWriter(loggedInMember, writer, pageRequest);
    }

    public List<PostResponseDTO> showBookmarkedPosts(Member member, PageRequest pageRequest) {
        return communityPostService.showBookmarkedPosts(member, pageRequest);
    }
}
