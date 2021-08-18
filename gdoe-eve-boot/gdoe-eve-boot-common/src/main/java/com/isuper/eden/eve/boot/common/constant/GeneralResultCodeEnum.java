package com.isuper.eden.eve.boot.common.constant;

import com.isuper.eden.eve.boot.common.utils.ResourceUtils;

import java.util.Locale;

/**
 * @author admin
 */

public enum GeneralResultCodeEnum {

  /** 通用返回成功标识 */
  SUCCESS("00000000", EveSystemConstant.RESULT_CODE_PREFIX + "success"),

  /** 默认的系统异常/未知的系统异常 */
  SYSTEM_FAIL_DEFAULT("00001000", EveSystemConstant.RESULT_FAIL_CODE_PREFIX + "default"),

  /** 默认的系统错误/未知的系统错误 */
  SYSTEM_ERROR_DEFAULT("00002000", EveSystemConstant.RESULT_ERROR_CODE_PREFIX + "default"),

  /** 默认的业务错误/未知的业务错误 */
  SYSTEM_BIZ_DEFAULT("00003000", EveSystemConstant.RESULT_BIZ_ERROR_CODE_PREFIX + "default"),



  /** 系统异常 - 网络请求错误 - 请求参数过大*/
  REQUEST_DATA_TOO_BIG("00001010", EveSystemConstant.RESULT_FAIL_CODE_PREFIX + "req.toobig"),

  /** 系统异常 - 网络请求错误 - 请求描述错误*/
  REQUEST_MULTIPART_ERROR("00001011", EveSystemConstant.RESULT_FAIL_CODE_PREFIX + "req.multipart"),

  /** 系统异常 - 网络请求错误 - HttpMessageNotReadableException */
  REQUEST_MSG_NOT_READABLE(
          "00001012", EveSystemConstant.RESULT_FAIL_CODE_PREFIX + "req.not.readable"),

  /** 系统异常 - 网络请求错误 - HttpRequestMethodNotSupportedException */
  REQUEST_METHOD_NOT_SUPPORTED(
          "00001013", EveSystemConstant.RESULT_FAIL_CODE_PREFIX + "req.method.not.supported"),

  /** 系统异常 - 网络请求错误 - HttpMediaTypeNotSupportedException */
  REQUEST_MEDIA_NOT_SUPPORTED(
          "00001014", EveSystemConstant.RESULT_FAIL_CODE_PREFIX + "req.media.not.supported"),

  /** 系统异常 - 网络请求错误 - BindException */
  REQUEST_BIND_ERROR("00001015", EveSystemConstant.RESULT_FAIL_CODE_PREFIX + "req.bind.error"),




  /** 业务错误 - 数据校验错误 */
  VAILD_ERROR("00003010", EveSystemConstant.RESULT_CODE_PREFIX + "vaild"),
  ;

  private String resultCode;

  private String resultType;

  public String getResultCode() {
    return resultCode;
  }

  public String getResultType() {
    return resultType;
  }

  public String getResultMsg(Locale locale) {
    try {
      return ResourceUtils.getBundleAsStringForBaseResultCode(locale, resultType);
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public String getResultMsg(Locale locale, String defaultMsg) {
    try {
      return ResourceUtils.getBundleAsStringForBaseResultCode(locale, resultType);
    } catch (Exception e) {
      return defaultMsg;
    }
  }

  public String getResultMsg() {
    return ResourceUtils.getBundleAsStringForBaseResultCode(Locale.getDefault(), resultType);
  }

  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

  public void setResultType(String resultType) {
    this.resultType = resultType;
  }

  GeneralResultCodeEnum(String resultCode, String resultType) {
    this.resultCode = resultCode;
    this.resultType = resultType;
  }
}
