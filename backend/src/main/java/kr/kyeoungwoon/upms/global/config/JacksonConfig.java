package kr.kyeoungwoon.upms.global.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
      // Long을 String으로
      builder.serializerByType(Long.class, ToStringSerializer.instance);
      builder.serializerByType(Long.TYPE, ToStringSerializer.instance);

      // null 필드 제외
//      builder.serializationInclusion(JsonInclude.Include.NON_NULL);

      // 빈 컬렉션 제외
//      builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
    };
  }
}
