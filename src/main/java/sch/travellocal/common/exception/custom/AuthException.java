package sch.travellocal.common.exception.custom;

import sch.travellocal.common.exception.error.ErrorCode;

public class AuthException extends BusinessBaseException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
