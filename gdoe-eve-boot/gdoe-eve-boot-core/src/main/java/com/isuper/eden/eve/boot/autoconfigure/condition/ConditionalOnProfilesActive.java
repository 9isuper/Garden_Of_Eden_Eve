package com.isuper.eden.eve.boot.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/** @author admin */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnProfilesActiveCondition.class)
public @interface ConditionalOnProfilesActive {

  // 约定的环境信息，优先级比配置低。 prod、sit、dev等
  String[] profilesActives() default {};

  // 通过配置文件中的配置名称获取
  String property() default "";

  // 匹配后是否注册bean，true：匹配被注册，false：未匹配的环境被注册
  boolean enabled() default true;
}
