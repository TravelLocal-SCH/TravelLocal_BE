package sch.travellocal.common.exception.custom;

import lombok.Getter;
import sch.travellocal.common.exception.error.ErrorCode;

@Getter
public class BusinessBaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
