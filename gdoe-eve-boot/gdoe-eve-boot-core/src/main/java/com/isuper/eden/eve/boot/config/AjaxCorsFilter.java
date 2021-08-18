package com.isuper.eden.eve.boot.config;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 */
@Configuration
@Slf4j
@ConditionalOnProperty(
    name = GdoeEveSystemProperties.PREFIX + ".corsEnable",
    havingValue = "true",
    matchIfMissing = true)
public class AjaxCorsFilter {

  @Resource GdoeEveSystemProperties fsgplusEveProperties;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CorsFilter corsFilter() {
    log.info("enable cors !");
    return new CorsFilter(
        configurationSource(
            Splitter.on(",").splitToList(this.fsgplusEveProperties.getAjaxCorsAllowedHeaders()),
            Splitter.on(",").splitToList(this.fsgplusEveProperties.getAjaxCorsExposedHeaders()),
            Splitter.on(",").splitToList(this.fsgplusEveProperties.getAjaxCorsAllowedMethods()),
            Splitter.on(",")
                .splitToList(this.fsgplusEveProperties.getAjaxCorsAllowedOriginPatterns()),
            this.fsgplusEveProperties.getAjaxCorsPattern()));
  }

  private static UrlBasedCorsConfigurationSource configurationSource(
      List<String> allowedHeaders,
      List<String> exposedHeaders,
      List<String> allowedMethods,
      List<String> allowedOriginPatterns,
      String pattern) {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedHeaders(allowedHeaders);
    corsConfig.setAllowedMethods(allowedMethods);
    corsConfig.setAllowedOriginPatterns(allowedOriginPatterns);
    corsConfig.setExposedHeaders(exposedHeaders);
    corsConfig.setMaxAge(36000L);
    corsConfig.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(pattern, corsConfig);
    return source;
  }
}
