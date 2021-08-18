package com.isuper.eden.eve.boot.config;

import com.isuper.eden.eve.boot.common.utils.SystemVersionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** @author admin */
@Setter
@Getter
@ToString
@Component
@ConfigurationProperties(prefix = EveProperties.PREFIX)
public class EveProperties {

  public static final String PREFIX = "gdoe.config.system";

  /** 返回数据的Status是不是总是Http200 */
  private boolean allwayHttpstatusOk = true;

  /** 默认是false，避免用在生产环境 */
  private boolean isSwaggerEnable = false;

  private String swaggerTitle = "";
  private String swaggerDescription = "上海外服云信息技术有限公司";
  private String swaggerContactName = "云公司";
  private String swaggerContactUrl = "https://www.9isuper.com/";
  private String swaggerContactEmail = "admin@9isuper.com";
  private String swaggerShowVersion = SystemVersionUtils.getSystemVersion(this.getClass());

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
}
