package com.isuper.eden.eve.boot.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isuper.eden.eve.boot.common.constant.EveSystemConstant;
import com.isuper.eden.eve.boot.common.constant.GeneralResultCodeEnum;
import com.isuper.eden.eve.boot.common.holder.RequestHolder;
import com.isuper.eden.eve.boot.common.holder.RequestHolderConstant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;
import java.util.Map;

/**
 * @author admin
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {

  /** 返回的数据编码 */
  @JsonProperty("resultCode")
  private String resultCode;

  /** 返回的消息 */
  @JsonProperty("message")
  private String message;

  /** 数据验证校验结果 */
  @JsonProperty("verify")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private Map<String, String> checkStack = null;

  /** 服务提供者 */
  @JsonProperty("provider")
  private String provider = EveSystemConstant.SYSTEM_SERVICE_ID;

  ;

  /** 返回的数据 */
  @JsonProperty("obj")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private T obj;

  @JsonProperty("responseTime")
  private long responseTime = System.currentTimeMillis() / 1000;

  public RestResponse(String resultCode, String message, T obj) {
    this.obj = obj;
    this.message = message;
    this.resultCode = resultCode;
  }

  public void setRestResult(String resultCode, String message, T obj) {
    this.obj = obj;
    this.message = message;
    this.resultCode = resultCode;
  }

  public RestResponse(Map<String, String> checkStack, String resultCode, String message) {
    this.obj = null;
    this.resultCode = resultCode;
    this.message = message;
    this.checkStack = checkStack;
  }

  public RestResponse(GeneralResultCodeEnum generalResultCodeEnum) {
    this.resultCode = generalResultCodeEnum.getResultCode();
    this.message = generalResultCodeEnum.getResultMsg();
  }

  public RestResponse addObj(T obj) {
    this.obj = obj;
    return this;
  }

  public RestResponse(GeneralResultCodeEnum generalResultCodeEnum, Locale locale) {
    if (locale == null) {
      locale = Locale.getDefault();
    }
    this.resultCode = generalResultCodeEnum.getResultCode();
    this.message = generalResultCodeEnum.getResultMsg(locale);
  }

  public RestResponse(GeneralResultCodeEnum generalResultCodeEnum, Map<String, String> checkStack) {
    this.resultCode = generalResultCodeEnum.getResultCode();
    this.message =
        generalResultCodeEnum.getResultMsg(
            (Locale)
                RequestHolder.get(
                    RequestHolderConstant.REQUET_CLIENT_LANGUAGE, Locale.getDefault()));
    this.checkStack = checkStack;
  }
}
