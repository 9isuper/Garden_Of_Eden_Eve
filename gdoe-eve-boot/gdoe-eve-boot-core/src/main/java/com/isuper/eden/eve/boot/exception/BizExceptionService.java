package com.isuper.eden.eve.boot.exception;

import com.isuper.eden.eve.boot.common.exception.EveBizException;

/** @author admin */
public interface BizExceptionService {

  /**
   * 通过错误的CODE信息获取国际化异常描述
   *
   * @param code 异常编码
   * @return 异常对象
   */
  default EveBizException getExceptionFromCode(String code) {
    return getExceptionFromCode(code, null);
  }

  /**
   * 通过错误的CODE信息获取国际化异常描述
   *
   * @param code 异常编码
   * @param message 异常消息，如果未对应消息，则显示此消息
   * @return 异常对象
   */
  default EveBizException getExceptionFromCode(String code, String message) {
    return getExceptionFromCode(code, null, message);
  }

  /**
   * 通过错误的CODE信息获取国际化异常描述
   *
   * @param code 异常编码
   * @param defaultResultCode 匹配的返回错误编码，如果查询不到code，则使用改Code信息
   * @param defaultMsg 异常消息，如果未对应消息，则显示此消息
   * @return 异常对象
   */
  EveBizException getExceptionFromCode(
      String code, String defaultResultCode, String defaultMsg);

  /**
   * 通过错误的CODE信息获取国际化异常描述
   *
   * @param code 异常编码
   * @param defaultResultCode 匹配的返回错误编码，如果查询不到code，则使用改Code信息
   * @param defaultMsg 异常消息，如果未对应消息，则显示此消息
   * @param arguments 消息错误中需要替换的参数
   * @return 异常对象
   */
  EveBizException getExceptionFromCode(
      String code, String defaultResultCode, String defaultMsg, Object... arguments);
}
