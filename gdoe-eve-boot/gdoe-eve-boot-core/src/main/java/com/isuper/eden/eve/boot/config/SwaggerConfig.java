package com.isuper.eden.eve.boot.config;

import com.isuper.eden.eve.boot.common.utils.SystemVersionUtils;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/** @author admin */
@EnableOpenApi
@Configuration
public class SwaggerConfig {

  @Bean
  public Docket createRestApi(EveProperties eveProperties) {
    return new Docket(DocumentationType.OAS_30)
        .apiInfo(apiInfo(eveProperties))
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo(EveProperties eveProperties) {
    return new ApiInfoBuilder()
        .title(eveProperties.getSwaggerTitle())
        .description(eveProperties.getSwaggerDescription())
        .contact(
            new Contact(
                eveProperties.getSwaggerContactName(),
                eveProperties.getSwaggerContactUrl(),
                eveProperties.getSwaggerContactEmail()))
        .version(eveProperties.getSwaggerShowVersion())
        .build();
  }
}
