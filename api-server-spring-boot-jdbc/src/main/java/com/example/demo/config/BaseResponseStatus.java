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

    //post /app/users/login
    POST_USERS_EMPTY_ID(false,2028,"전화번호, 이메일 주소 또는 사용자 이름을 입력해주세요."),
    POST_USERS_ID_NOT_EXIST(false,2029,"해당 전화번호, 이메일 주소 또는 사용자 이름을 가진 유저가 존재하지 않습니다."),
    POST_USERS_ACCOUNT_INACTIVE(false,2030,"해당 계정은 비활성화 상태입니다."),
    POST_USERS_ACCOUNT_DELETED(false,2031,"해당 계정은 더이상 존재하지 않습니다."),

    //get /app/users/:user-id
    GET_USERS_INVALID_USER_ID(false,2032, "존재하지 않는 유저입니다."),

    //follows
    GET_FOLLOWS_NO_CONNECTED_FOLLOWS(false,2034,"함께 아는 친구가 존재하지 않습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


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
