package com.isuper.eden.eve.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author admin
 */
@RestController
@RequestMapping("health")
@Api(value = "系统健康检查", tags = "系统健康检查")
public class HealthController {

    @ApiOperation(value = "发起ping请求,测试系统是运行", notes = "该ping请求支持POST、GET方式")
    @RequestMapping(value = "ping", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String ping() {
        return "pong";
    }

}
