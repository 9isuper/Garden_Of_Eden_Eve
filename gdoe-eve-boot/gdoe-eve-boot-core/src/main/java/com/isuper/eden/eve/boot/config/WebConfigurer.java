package com.isuper.eden.eve.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author admin */
@Slf4j
@SuppressWarnings("deprecation")
@Configuration
public class WebConfigurer implements WebMvcConfigurer, ServletContextInitializer {

  @Autowired private Environment env;

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    log.info(
        "Web application configuration, using profiles: {}",
        Arrays.toString(env.getActiveProfiles()));
    log.info("Web application fully configured");
  }

  /** Set up Mime types. */
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.favorPathExtension(false);
    configurer.favorParameter(true);
    configurer.parameterName("mediaType");
    configurer.ignoreAcceptHeader(true);
    configurer.useJaf(false);
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
    Map<String, MediaType> mediaTypes = new HashMap<>();
    mediaTypes.put("json", MediaType.APPLICATION_JSON);
    mediaTypes.put("xml", MediaType.APPLICATION_XML);
    mediaTypes.put("html", MediaType.TEXT_HTML);
    mediaTypes.put("atom", MediaType.APPLICATION_ATOM_XML);
    configurer.mediaTypes(mediaTypes);
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new FormHttpMessageConverter());
  }
}
