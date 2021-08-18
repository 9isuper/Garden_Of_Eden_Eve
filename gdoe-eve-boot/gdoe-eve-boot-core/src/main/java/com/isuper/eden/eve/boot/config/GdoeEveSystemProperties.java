package com.isuper.eden.eve.boot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author admin
 */
@Setter
@Getter
@ToString
@Component
@ConfigurationProperties(prefix = GdoeEveSystemProperties.PREFIX)
public class GdoeEveSystemProperties {

  public static final String PREFIX = "gdoe.eve.system";

  /** 返回数据的Status是不是总是Http200 */
  private boolean allwayHttpstatusOk = true;

  /** 默认是false，避免用在生产环境 */
  private boolean isSwaggerEnable = false;

  private String swaggerTitle = "伊甸园核心框架系统";
  private String swaggerDescription = "9isuper开发团队";
  private String swaggerContactName = "技术支持：9isuper";
  private String swaggerContactUrl = "https://www.9isuper.com/";
  private String swaggerContactEmail = "yongle.zou@9isuper.com";

  /** 是否开启跨域问题处理 */
  private boolean corsEnable = false;

  private String ajaxCorsAllowedHeaders =
      "x-auth-token,content-type,X-Requested-With,XMLHttpRequest";
  private String ajaxCorsExposedHeaders =
      "x-auth-token,content-type,X-Requested-With,XMLHttpRequest";
  private String ajaxCorsAllowedMethods = "POST,GET,DELETE,PUT,PATCH,OPTIONS";
  private String ajaxCorsAllowedOriginPatterns = "*";
  private String ajaxCorsPattern = "/**";

  /** 国际化异常信息资源文件存储的位置 */
  private String errorMsgLocalPath = "error/i18n/errorMsg";

  /** 异常编码对照表存放位置 */
  private String errorCodeLocalPath = "error/errorcode";

  /** 缺省返回的错误编码信息 */
  private String defaultResultErrorCode = "99999999";

  /** 缺省返回的错误编码消息 */
  private String defaultResultErrorMassage = "error!";

  /** 缺省返回的成功编码信息 */
  private String defaultResultSuccessCode = "00000000";

  /** 缺省返回的成功编码消息 */
  private String defaultResultSuccessMassage = "success!";
}
