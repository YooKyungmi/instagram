package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /app/users
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2015, "전화번호를 입력해주세요."),
    POST_USERS_EMPTY_EMAIL_ADDRESS(false, 2016, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL_ADDRESS(false, 2017, "이메일 형식을 확인해주세요."),
    POST_USERS_INVALID_PHONE_NUMBER(false,2018,"전화번호 형식을 확인해주세요."),
    POST_USERS_EMPTY_BIRTH_DATE(false,2019,"생년월일을 입력해주세요."),
    POST_USERS_INVALID_BIRTH_DATE(false,2020,"유효하지 않은 생년월일입니다."),
    POST_USERS_INVALID_BIRTH_DATE_FORMAT(false,2033,"생년월일 형식을 확인해주세요."),
    POST_USERS_EMPTY_NICKNAME(false,2021,"닉네임을 입력해주세요."),
    POST_USERS_INVALID_NICKNAME(false,2022, "닉네임에는 영문자, 숫자, 마침표, 밑줄만 사용 가능합니다."),
    POST_USERS_EMPTY_PASSWORD(false,2023,"비밀번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false,2024,"비밀번호를 6자 이상 입력해주세요."),
    POST_USERS_EXISTS_EMAIL_ADDRESS(false,2025,"중복된 이메일입니다."),
    POST_USERS_EXISTS_PHONE_NUMBER(false,2026,"중복된 전화번호입니다."),
    POST_USERS_EXISTS_NICKNAME(false,2027,"중복된 닉네임입니다."),

    //[POST] /app/users/login
    POST_USERS_EMPTY_ID(false,2028,"전화번호, 이메일 주소 또는 사용자 이름을 입력해주세요."),
    POST_USERS_ID_NOT_EXIST(false,2029,"해당 전화번호, 이메일 주소 또는 사용자 이름을 가진 유저가 존재하지 않습니다."),
    POST_USERS_ACCOUNT_INACTIVE(false,2030,"해당 계정은 비활성화 상태입니다."),
    POST_USERS_ACCOUNT_DELETED(false,2031,"해당 계정은 더이상 존재하지 않습니다."),

    //[GET] /app/users/:user-id
    GET_USERS_INVALID_USER_ID(false,2032, "존재하지 않는 유저입니다."),

    //[GET] /app/users/identifications/:userId
    GET_USERS_EMPTY_PASSWORD(false,2038,"비밀번호를 입력해주세요."),

    //[GET] /app/users?user-nickname=
    GET_USERS_EMPTY_NICKNAME(false,2043,"검색할 닉네임을 입력해주세요."),

    //[PATCH] /app/users/accounts/:user-id?account-status=
    PATCH_USERS_EMPTY_ACCOUNT_STATUS(false,2037,"변경할 계정 상태 값을 입력해주세요."),

    //follows

    //[GET] /app/follows/connected-follows?user-id=
    GET_FOLLOWS_NO_CONNECTED_FOLLOWS(false,2034,"함께 아는 친구가 존재하지 않습니다."),
    GET_FOLLOWS_NO_CONNECTED_FOLLOWS_FOR_ONE_SELF(false,2039,"본인에 대해서는 함께 아는 친구 정보를 제공하지 않습니다."),

    //[POST] /app/follows/connected-follows?user-id=
    //[PATCH] /app/follows/connected-follows?user-id=
    PATCH_FOLLOWS_NOT_EXIST(false,2036,"해당 유저를 팔로우하고 있지 않습니다."),

    //UserStory
    //[GET] /app/stories?user-id=
    GET_STORIES_EMPTY_USER_ID(false,2040,"유저 식별자를 입력해주세요."),

    //[GET] /app/stories/:story-id
    GET_STORIES_EMPTY_STORY_ID(false,2041,"스토리 식별자를 입력해주세요."),
    GET_STORIES_STORY_ID_NOT_EXISTS(false,2042,"존재하지 않는 스토리입니다."),

    //[PATCH] /app/stories/:story-id
    PATCH_STORIES_EMPTY_STORY_ID(false,2044,"스토리 식별자를 입력해주세요."),

    //[PATCH] /app/stories/likes/:story-id?user-id= & like-status =
    PATCH_STORIES_EMPTY_USER_ID(false,2045,"유저 식별자를 입력해주세요."),
    PATCH_STORIES_EMPTY_LIKE_STATUS(false,2046,"좋아요 상태값을 입력해주세요."),
    GET_STORIES_STORY_VIEWER_NOT_EXISTS(false, 2047,"조회한 적 없는 스토리입니다."),

    //highlight

    //[POST] /app/highlights
    POST_HIGHLIGHTS_EMPTY_STORY_ID_LIST(false,2048,"하이라이트로 생성할 스토리를 골라주세요."),
    POST_HIGHLIGHTS_INVALID_STORY_ID(false, 2049, "존재하지 않는 스토리 식별자입니다."),
    POST_HIGHLIGHTS_INVALID_STORY_JWT(false,2050,"해당 스토리는 유저에게 접근 권한이 없습니다."),

    //[GET] /app/highlights?user-id=
    GET_HIGHLIGHTS_EMPTY_USER_ID(false,2051,"유저 식별자를 입력해주세요."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    FAILED_TO_IDENTIFY(false,3015,"비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /app/users/profiles/:user-id
    //[PATCH] /app/users/accounts/:user-id?account-status=
    //[PATCH] /app/users/user-infos/email-addresses/:user-id
    //[PATCH] /app/users/user-infos/phone-numbers/:user-id
    MODIFY_FAIL_USER(false,4014,"유저 정보 수정에 실패하였습니다."),

    //follow

    //[POST] /app/follows/connected-follows?user-id=
    //[PATCH] /app/follows/connected-follows?user-id=
    MODIFY_FAIL_USER_FOLLOW(false,4015,"팔로우 추가/삭제에 실패하였습니다."),

    //story

    //[PATCH] /app/stories/:story-id
    MODIFY_FAIL_USER_STORY(false,4017,"스토리 삭제에 실패하였습니다."),

    //[PATCH] /app/stories/likes/:story-id?user-id= & like-status =
    MODIFY_FAIL_STORY_VIEWER_LIKE(false,4018,"조회한 스토리의 좋아요 수정에 실패하였습니다."),


    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    // [POST] /app/posts
    POST_FAILED(false, 4016, "요청하신 데이터를 추가할 수 없습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
