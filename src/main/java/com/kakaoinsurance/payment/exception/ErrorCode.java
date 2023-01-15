package com.kakaoinsurance.payment.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    NUMBER_NOT_FOUND(404, "관리번호가 존재하지 않습니다"),
    ALREADY_SAVED_NUMBER(409, "이미 존재하는 관리번호입니다. 다시 시도해주십시오."),
    ALREADY_CANCEL(409, "이미 취소 처리된 데이터입니다"),

    DO_NOT_PARTIAL_CANCEL(409, "부분 취소를 할 수 없습니다. 금액을 확인해주십시오"),

    INTERNAL_SERVER_ERROR(500, "서버에서 오류가 발생했습니다.");

    private final int status;
    private final String message;
}
