package kr.kyeoungwoon.upms.domain.test.dto;

import lombok.Builder;

public class TestRequestDto {

  @Builder
  public record TestTokenRequestDto(
      Long challengerId
  ) {

  }
}
