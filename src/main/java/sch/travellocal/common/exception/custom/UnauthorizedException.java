package sch.travellocal.common.exception.custom;

import sch.travellocal.common.exception.error.ErrorCode;

public class UnauthorizedException extends BusinessBaseException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
