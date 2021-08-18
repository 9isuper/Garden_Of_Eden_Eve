package com.isuper.eden.eve.boot.common.constant;

import com.isuper.eden.eve.common.constant.SystemConstant;

/**
 * @author admin
 */
public class EveSystemConstant extends SystemConstant {

  /** 伊甸园-夏娃BOOT-程序配置前缀 */
  public static final String EVE_CONFIG_PREFIX = "com.isuper.eden.eve.";

  /** 系统ID */
  public static String SYSTEM_SERVICE_ID = "0000000000";

  /** 系统名称 */
  public static String SYSTEM_SERVICE_NAME = "default-eve-boot";

  /** 国际化返回编码Code前缀 */
  public static String RESULT_CODE_PREFIX = "eve.result.msg.";

  /** 国际化返回编码Code前缀 - 调用失败 */
  public static String RESULT_FAIL_CODE_PREFIX = EveSystemConstant.RESULT_CODE_PREFIX + "fail.";

  /** 国际化返回编码Code前缀 - 调用错误、异常 */
  public static String RESULT_ERROR_CODE_PREFIX = EveSystemConstant.RESULT_CODE_PREFIX + "error.";

  /** 国际化返回编码Code前缀 - 业务结果 */
  public static String RESULT_BIZ_ERROR_CODE_PREFIX = EveSystemConstant.RESULT_CODE_PREFIX + "biz.";

  /** 国际化资源文件存储的路径 */
  public static String ERROR_RESOURCE_PATH = "i18n/error/errorMsg";
}
