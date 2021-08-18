package com.isuper.eden.eve.boot.common.holder;

/**
 * @author admin
 */
public class RequestHolderConstant {

    /**
     * 客户端的区域语言 - 用作国际化
     */
    public static final String REQUET_CLIENT_LANGUAGE = "client_language";

    /**
     * 请求接收的时间 - 用作度量接口响应时间
     */
    public static final String REQUEST_TIMESTAMP = "request_timestamp";

    /**
     * 请求的事件ID
     */
    public static final String REQUEST_EVENT_ID = "request_eventid";

    /**
     * 记录请求的消费者
     */
    public static final String REQUEST_CONSUMER = "request_consumer";

    /**
     * 记录本次请求REQUEST对象的HASH值，确认为此次请求事件
     */
    public static final String REQUEST_HASH = "request_hash";


}
