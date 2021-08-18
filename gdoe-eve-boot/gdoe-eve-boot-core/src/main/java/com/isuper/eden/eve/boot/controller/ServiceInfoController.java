package com.isuper.eden.eve.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


/**
 * @author admin
 */
@RestController
@RequestMapping("server")
@Api(value = "系统信息服务", tags = "系统信息服务")
public class ServiceInfoController {

    @ApiOperation(value = "获取当前服务器时间", notes = "获取当前服务器时间")
    @RequestMapping(value = "dateTime", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
