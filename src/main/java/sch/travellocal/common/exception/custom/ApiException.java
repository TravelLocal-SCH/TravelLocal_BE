package sch.travellocal.common.exception.custom;

import lombok.Getter;
import sch.travellocal.common.exception.error.ErrorCode;

@Getter
public class ApiException extends BusinessBaseException {

    public ApiException(ErrorCode errorCode, String message) {
      super(errorCode, message);
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
