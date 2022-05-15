package com.gts.godtingDev.config.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionMessage{

    // 400 BAD_REQUEST
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다."),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰 정보가 일치하지 않습니다."),
    FILE_UPLOAD_FAILED(BAD_REQUEST, "파일 업로드에 실패하였습니다."),
    NOT_SUPPORTED_LOGIN_PROCESS(BAD_REQUEST, "지원하지 않는 로그인 형식입니다."),

    // 401 UNAUTHORIZED
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),
    UNAUTHORIZED_CHAT(UNAUTHORIZED, "채팅 권한이 없는 대상입니다."),
    UNAUTHORIZED_EMAIL(UNAUTHORIZED, "이메일 인증이 진행되지 않았습니다."),

    // 404 NOT_FOUND
    MEMBER_NOT_FOUND(NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다."),
    CHAT_NOT_FOUND(NOT_FOUND, "채팅 대상을 찾을 수 없습니다."),

    // 404 NOT_FOUND, OAuth2 Provider Error
    OAUTH2_PROVIDER_NOT_FOUND(NOT_FOUND, "잘못된 프로바이더 입니다."),
    // 수정 필요할 수 있는 코드, 메일 인증 요청시간 초과로 임시 설정함.
    MAIL_AUTHORIZED_CODE_NOT_FOUND(NOT_FOUND, "메일 인증코드를 찾을 수 없습니다."),
    URL_NOT_MATCHED(NOT_FOUND, "잘못된 URL 주소입니다."),

    // 409 CONFLICT, Database Error
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다."),
    REDUPLICATION_EMAIL(CONFLICT, "이미 존재하는 이메일입니다."),
    REDUPLICATION_NICKNAME(CONFLICT, "이미 존재하는 닉네임입니다."),

    // 410 GONE
    FILE_DELETED(GONE, "파일이 사용자에 의해 삭제되었습니다."),

    // 500 INTERNAL_SERVER_ERROR
    SERVER_FILE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "서버 오류로 파일 업로드에 실패했습니다."),
    SERVER_MAIL_SEND_FAILED(INTERNAL_SERVER_ERROR, "서버 오류로 인해 메일 전송에 실패했습니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    SERVER_OAUTH2_ACCESS_TOKEN_ERROR(INTERNAL_SERVER_ERROR, "엑세스 토큰을 통해 서버와 통신하던 중 오류가 발생했습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
