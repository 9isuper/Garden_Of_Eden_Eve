package com.isuper.eden.eve.boot.aop;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.isuper.eden.eve.boot.common.holder.RequestHolder;
import com.isuper.eden.eve.boot.common.holder.RequestHolderConstant;
import com.isuper.eden.eve.boot.common.utils.EventIdUtil;
import com.isuper.eden.eve.boot.common.utils.JsonMapper;
import com.isuper.eden.eve.common.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Locale;

/** @author admin */
@Aspect
@Slf4j
@Component
public class HttpRequestAop implements Ordered {

  @Pointcut("execution(public * com.isuper..controller.*.*(..)))")
  public void HttpRequestAop() {}


  // 前置通知
  @Before("HttpRequestAop()")
  public void doBeforeFunction(JoinPoint joinPoint) {
    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (!String.valueOf(requestAttributes.getRequest().hashCode())
        .equals(String.valueOf(RequestHolder.get(RequestHolderConstant.REQUEST_HASH)))) {
      RequestHolder.add(
          RequestHolderConstant.REQUEST_HASH, requestAttributes.getRequest().hashCode());
      RequestHolder.add(RequestHolderConstant.REQUEST_TIMESTAMP, System.currentTimeMillis());
      RequestHolder.add(
          RequestHolderConstant.REQUEST_EVENT_ID, EventIdUtil.getInstance().generateEventId());
      MDC.put(
          RequestHolderConstant.REQUEST_EVENT_ID,
          RequestHolder.get(RequestHolderConstant.REQUEST_EVENT_ID).toString());
    }

    Locale locale = LocaleContextHolder.getLocale();
    String language = requestAttributes.getRequest().getParameter("language");
    if (StringUtils.isNotBlank(language)) {
      List<String> localeInfo =
          Lists.newArrayList(
              Splitter.on(SystemConstant.SEPARATOR_UNDERLINE)
                  .split(
                      language.replaceAll(
                          SystemConstant.SEPARATOR_SHORT_LINE,
                          SystemConstant.SEPARATOR_UNDERLINE)));
      if (localeInfo.size() > 1) {
        locale = new Locale(localeInfo.get(0).toLowerCase(), localeInfo.get(1).toUpperCase());
      }
    }
    RequestHolder.add(RequestHolderConstant.REQUET_CLIENT_LANGUAGE, locale);
    log.info(
        "method：{} , language: {} , uri: {}",
        joinPoint.getSignature().getName(),
        locale.getLanguage(),
        requestAttributes.getRequest().getRequestURI());
  }

  // 后置通知
  @After("HttpRequestAop()")
  public void doAfterFunction(JoinPoint joinPoint) {
    log.info(
        "used time: {} s",
        (System.currentTimeMillis()
                - (Long) RequestHolder.get(RequestHolderConstant.REQUEST_TIMESTAMP))
            / 1000);
  }

  // 返回通知
  @AfterReturning(value = "HttpRequestAop()", returning = "result")
  public void doAfterReturningFunction(JoinPoint joinPoint, Object result) {
    if (ObjectUtils.isNotEmpty(result)) {
      log.info("return: {}", JsonMapper.getInstance().toJson(result));
    }
    RequestHolder.remove();
  }

  // 异常通知
  @AfterThrowing(value = "HttpRequestAop()", throwing = "ex")
  public void doAfterThrowingFunction(JoinPoint joinPoint, Throwable ex) {
    log.warn("request has error {} ", ex.getMessage());
    RequestHolder.remove();
  }

  @Override
  public int getOrder() {
    return Integer.MIN_VALUE;
  }
}
