package com.isuper.eden.eve.boot.config;

import com.isuper.eden.eve.boot.exception.BizExceptionService;
import com.isuper.eden.eve.boot.exception.impl.BizSouceExceptionServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** @author admin */
@Component
@Order(Ordered.LOWEST_PRECEDENCE + 100)
public class EveSystemExceptionConfiguration {

  @Bean
  @ConditionalOnMissingBean(BizExceptionService.class)
  public BizExceptionService fsgBizExceptionService(EveProperties eveProperties) {
    return new BizSouceExceptionServiceImpl(eveProperties);
  }
}
