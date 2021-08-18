package com.isuper.eden.eve.boot.common.utils;

import com.isuper.eden.eve.common.constant.SystemConstant;

import java.util.UUID;

/**
 * @author admin
 */
public class EventIdUtil {

  private static EventIdUtil eventIdUtil;

  public static synchronized EventIdUtil getInstance() {
    if (eventIdUtil == null) {
      eventIdUtil = new EventIdUtil();
    }
    return eventIdUtil;
  }


  /**
   * generateEventId
   * <p>生成事件ID<br>
   * @return 事件ID
   */
  public String generateEventId() {
    return UUID.randomUUID()
        .toString()
        .replaceAll(SystemConstant.SEPARATOR_SHORT_LINE, "")
        .toLowerCase();
  }
}
