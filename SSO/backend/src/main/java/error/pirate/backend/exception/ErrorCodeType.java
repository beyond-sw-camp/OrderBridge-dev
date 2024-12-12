package error.pirate.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType {

    EXIST_USER(HttpStatus.BAD_REQUEST, "USER_ERROR_001", "해당 아이디는 이미 존재하는 아이디입니다."),
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}