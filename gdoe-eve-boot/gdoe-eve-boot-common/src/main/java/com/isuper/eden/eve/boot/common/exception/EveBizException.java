package com.isuper.eden.eve.boot.common.exception;

import com.isuper.eden.eve.boot.common.holder.RequestHolder;
import com.isuper.eden.eve.boot.common.holder.RequestHolderConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * @author admin
 */
@Setter
@Getter
public class EveBizException extends RuntimeException {

  /** 异常编码 */
  private String code;

  /** 异常返回的错误编号 */
  private String resultCode;

  /** 错误默认消息 */
  private String message;

  /** 事件ID */
  private String eventId =
      String.valueOf(RequestHolder.get(RequestHolderConstant.REQUEST_EVENT_ID));

  public EveBizException(String code, String resultCode, String message) {
    super(message);
    this.code = code;
    this.resultCode = resultCode;
    this.message = message;
  }

  public EveBizException(String code, String resultCode, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
    this.resultCode = resultCode;
    this.message = message;
  }

  public EveBizException(Throwable cause) {
    super(cause);
  }
}
