package kr.kyeoungwoon.upms.security;

import java.util.List;
import kr.kyeoungwoon.upms.global.enums.ChapterAdminRole;
import lombok.Builder;

@Builder
public record UserPrincipal(Long challengerId, List<ChapterAdminRole> roles) {

}