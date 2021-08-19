package com.isuper.eden.eve.boot.holder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** @author admin */
@Slf4j
@Component
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

  private static ApplicationContext applicationContext = null;

  /**
   * getApplicationContext
   *
   * <p>取得存储在静态变量中的ApplicationContext.<br>
   *
   * @return 结果
   */
  public static ApplicationContext getApplicationContext() {
    assertContextInjected();
    return applicationContext;
  }

  public static String getRootRealPath() {
    String rootRealPath = "";
    try {
      rootRealPath = getApplicationContext().getResource("").getFile().getAbsolutePath();
    } catch (IOException e) {
      log.error("获取系统根目录失败", e);
    }
    return rootRealPath;
  }

  public static String getResourceRootRealPath() {
    String rootRealPath = "";
    try {
      rootRealPath = new DefaultResourceLoader().getResource("").getFile().getAbsolutePath();
    } catch (IOException e) {
      log.error("获取资源根目录失败", e);
    }
    return rootRealPath;
  }

  /**
   * getBean
   *
   * <p>从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.<br>
   *
   * @param name name
   * @return 结果
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name) {
    assertContextInjected();
    return (T) applicationContext.getBean(name);
  }

  public static <T> T getBean(Class<T> requiredType) {
    assertContextInjected();
    return (T) applicationContext.getBean(requiredType);
  }

  public static <T> T getBean(String name, Class<T> requiredType) {
    assertContextInjected();
    return (T) applicationContext.getBean(name, requiredType);
  }

  /**
   * clearHolder
   *
   * <p>清除SpringContextHolder中的ApplicationContext为Null. <br>
   */
  public static void clearHolder() {
    if (log.isDebugEnabled()) {
      log.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
    }
    applicationContext = null;
  }

  /**
   * setApplicationContext
   *
   * <p>实现ApplicationContextAware接口, 注入Context到静态变量中.<br>
   *
   * @param applicationContext applicationContext
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {

    log.debug("注入ApplicationContext到SpringContextHolder:{0}", applicationContext);

    if (SpringContextHolder.applicationContext != null) {
      log.info(
          "SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:"
              + SpringContextHolder.applicationContext);
    }
    // NOSONAR
    SpringContextHolder.applicationContext = applicationContext;
  }

  /**
   * destroy
   *
   * <p>实现DisposableBean接口, 在Context关闭时清理静态变量. <br>
   */
  @Override
  public void destroy() throws Exception {
    SpringContextHolder.clearHolder();
  }

  /**
   * assertContextInjected
   *
   * <p>检查ApplicationContext不为空. <br>
   */
  private static void assertContextInjected() {
    Validate.validState(
        applicationContext != null,
        "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
  }
}
