package com.meowzip.apiserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumErrorCode {

    SUCCESS(1, "success"),
    BAD_REQUEST(400, "잘못된 요청입니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버에 오류가 발생하였습니다."),

    MEMBER_ALREADY_EXISTS(100001, "이미 존재하는 회원입니다."),
    TOKEN_REQUIRED(100002, "토큰이 없습니다."),
    TOKEN_EXPIRED(100003, "토큰이 만료되었습니다."),
    TOKEN_INVALID(100004, "유효하지 않은 토큰입니다."),
    MEMBER_NOT_FOUND(100005, "회원을 찾을 수 없습니다."),
    LOGIN_FAILED(100006, "아이디 또는 비밀번호를 확인해주세요."),
    TOKEN_NOT_FOUND(100007, "토큰을 찾을 수 없습니다."),
    NICKNAME_DUPLICATED(100008, "이미 사용 중인 닉네임입니다."),
    INVALID_NICKNAME(100009, "닉네임 정책에 맞지 않습니다."),

    IMAGE_UPLOAD_FAILED(200001, "이미지 업로드에 실패하였습니다."),
    IMAGE_NOT_FOUND(200002, "이미지를 찾을 수 없습니다."),

    DIARY_NOT_FOUND(300001, "일지를 찾을 수 없습니다."),

    CAT_NOT_FOUND(400001, "고양이를 찾을 수 없습니다."),
    INVALID_MET_AT(400002, "만난 날짜는 오늘 이후일 수 없습니다."),

    NOTIFICATION_TEMPLATE_NOT_FOUND(500001, "알림 템플릿을 찾을 수 없습니다."),

    CO_PARENT_NOT_FOUND(600001, "공동냥육 데이터를 찾을 수 없습니다."),
    CO_PARENT_ALREADY_PROCESSED(600002, "이미 처리된 공동냥육 신청입니다."),
    CO_PARENT_ALREADY_REQUESTED(600003, "이미 요청했습니다."),

    POST_NOT_FOUND(700001, "게시글을 찾을 수 없습니다."),
    ALREADY_BLOCKED(700002, "이미 차단된 회원입니다."),

    COMMENT_NOT_FOUND(800001, "댓글을 찾을 수 없습니다."),

    ALREADY_LIKED(900001, "이미 좋아요를 누르셨습니다."),
    NOT_LIKED(900002, "좋아요를 누르지 않았습니다."),

    ALREADY_BOOKMARKED(110001, "이미 북마크되어있는 게시물입니다."),
    NOT_BOOKMARKED(110002, "북마크하지 않은 게시물입니다."),

    ;

    private final int result;
    private final String message;
}
