package error.pirate.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType {

    // 게이트웨이 관련 오류
    GATE_WAY_BAD_GATEWAY_ERROR(HttpStatus.BAD_GATEWAY, "GATEWAY_ERROR_001", "서버가 응답하지 않습니다."),
    GATE_WAY_TIMEOUT_ERROR(HttpStatus.GATEWAY_TIMEOUT, "GATEWAY_ERROR_002", "게이트웨이 시간 초과"),

    // 스프링 시큐리티 관련 오류
    SECURITY_ACCESS_ERROR(HttpStatus.FORBIDDEN, "SECURITY_ERROR_001", "접근 권한이 없습니다."),
    SECURITY_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "SECURITY_ERROR_002", "로그인 후 다시 시도해 주세요."),
    SECURITY_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "SECURITY_ERROR_003", "로그인 실패하였습니다. 관리자에게 문의해 주세요."),

    // 주문서 관련 오류
    SALES_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "SALES_ORDER_ERROR_001", "주문서를 찾을 수 없습니다."),

    // 작업지시서 관련 오류
    WORK_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "WORK_ORDER_ERROR_001", "조회된 작업지시서가 없습니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "WORK_ORDER_ERROR_002", "유효하지 않은 날짜 범위입니다."),

    // 공통 오류
    COMMON_ERROR(HttpStatus.BAD_REQUEST, "COMMON_ERROR", "오류가 발생하였습니다. 관리자에게 문의 바랍니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}