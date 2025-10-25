package kr.kyeoungwoon.upms.global.apiPayload.exception;

import kr.kyeoungwoon.upms.global.apiPayload.code.BaseErrorCode;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import lombok.Getter;

@Getter
public class DomainException extends GeneralException {

  private final DomainType domainType;

  public DomainException(DomainType domainType, BaseErrorCode errorCode) {
    super(errorCode);
    this.domainType = domainType;
  }
}