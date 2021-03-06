package com.isuper.eden.eve.boot.exception.impl;

import com.isuper.eden.eve.boot.common.constant.GeneralResultCodeEnum;
import com.isuper.eden.eve.boot.common.exception.EveBizException;
import com.isuper.eden.eve.boot.common.holder.RequestHolder;
import com.isuper.eden.eve.boot.common.holder.RequestHolderConstant;
import com.isuper.eden.eve.boot.common.utils.ResourceUtils;
import com.isuper.eden.eve.boot.config.EveProperties;
import com.isuper.eden.eve.boot.exception.BizExceptionService;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.ResourceBundle;

/** @author admin */
public class BizSouceExceptionServiceImpl implements BizExceptionService {

  private EveProperties eveProperties;

  public BizSouceExceptionServiceImpl(EveProperties eveProperties) {
    this.eveProperties = eveProperties;
  }

  @Override
  public EveBizException getExceptionFromCode(
      String code, String defaultResultCode, String defaultMsg) {
    String errorMassage =
        this.getErrorMassage(
            code,
            StringUtils.isBlank(defaultMsg)
                ? GeneralResultCodeEnum.SYSTEM_BIZ_DEFAULT.getResultCode()
                : defaultMsg,
            this.eveProperties.getErrorMsgLocalPath(),
            null);
    return new EveBizException(
        code,
        this.getErrorCode(
            code,
            StringUtils.isBlank(defaultResultCode)
                ? GeneralResultCodeEnum.SYSTEM_BIZ_DEFAULT.getResultCode()
                : defaultResultCode,
            this.eveProperties.getErrorCodeLocalPath()),
        errorMassage);
  }

  @Override
  public EveBizException getExceptionFromCode(
      String code, String defaultResultCode, String defaultMsg, Object... arguments) {
    String errorMassage =
        this.getErrorMassage(
            code,
            StringUtils.isBlank(defaultMsg)
                ? GeneralResultCodeEnum.SYSTEM_BIZ_DEFAULT.getResultCode()
                : defaultMsg,
            this.eveProperties.getErrorMsgLocalPath(),
            arguments);
    return new EveBizException(
        code,
        this.getErrorCode(
            code,
            StringUtils.isBlank(defaultResultCode)
                ? GeneralResultCodeEnum.SYSTEM_BIZ_DEFAULT.getResultCode()
                : defaultResultCode,
            this.eveProperties.getErrorCodeLocalPath()),
        errorMassage);
  }

  /**
   * ??????????????????????????????????????????
   *
   * @param code ????????????
   * @param defaultResultCode ?????????????????????
   * @param codePropPath ????????????
   * @return String
   */
  private String getErrorCode(String code, String defaultResultCode, String codePropPath) {
    String errorCode = "";
    try {
      ResourceBundle resource = ResourceBundle.getBundle(codePropPath);
      errorCode = resource.getString(code);
    } catch (Exception e) {
    }
    if (StringUtils.isBlank(errorCode)) {
      errorCode = defaultResultCode;
    }
    return errorCode;
  }

  /**
   * ????????????????????????????????????
   *
   * @param code ????????????
   * @param defaultMassge ????????????
   * @param i18nPath ??????????????????
   * @param arguments ???????????????
   * @return String
   */
  private String getErrorMassage(
      String code, String defaultMassge, String i18nPath, Object... arguments) {
    String msg = "";
    try {
      if (arguments == null) {
        msg =
            ResourceUtils.getBundleAsString(
                i18nPath,
                (Locale)
                    RequestHolder.get(
                        RequestHolderConstant.REQUET_CLIENT_LANGUAGE, Locale.getDefault()),
                code);
      } else {
        msg =
            ResourceUtils.getBundleAsMessageString(
                i18nPath,
                (Locale)
                    RequestHolder.get(
                        RequestHolderConstant.REQUET_CLIENT_LANGUAGE, Locale.getDefault()),
                code,
                arguments);
      }
    } catch (Exception e) {
    }
    if (StringUtils.isBlank(msg)) {
      msg = defaultMassge;
    }
    return msg;
  }
}
