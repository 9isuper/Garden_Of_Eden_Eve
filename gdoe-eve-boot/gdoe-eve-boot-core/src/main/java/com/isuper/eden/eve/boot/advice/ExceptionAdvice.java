package com.isuper.eden.eve.boot.advice;

import com.google.common.collect.Maps;
import com.isuper.eden.eve.boot.common.constant.GeneralResultCodeEnum;
import com.isuper.eden.eve.boot.common.exception.EveBizException;
import com.isuper.eden.eve.boot.common.utils.JsonMapper;
import com.isuper.eden.eve.boot.common.utils.RestResponse;
import com.isuper.eden.eve.boot.config.GdoeEveSystemProperties;
import io.undertow.server.RequestTooBigException;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/** @author admin */
@ControllerAdvice
public class ExceptionAdvice {

  @Autowired GdoeEveSystemProperties gdoeEveSystemProperties;

  @ResponseBody
  @ExceptionHandler({EveBizException.class})
  public ResponseEntity<?> handlerByEveBizException(EveBizException ex) {
    return new ResponseEntity<RestResponse<?>>(
        new RestResponse<String>(
            StringUtils.isNotBlank(ex.getResultCode())
                ? ex.getResultCode()
                : GeneralResultCodeEnum.SYSTEM_FAIL_DEFAULT.getResultCode(),
            ex.getMessage(),
            ""),
        gdoeEveSystemProperties.isAllwayHttpstatusOk()
            ? HttpStatus.OK
            : HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseBody
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<?> handlerByValidException(MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    Map<String, String> validErrors = Maps.newHashMap();
    bindingResult.getFieldErrors().stream()
        .forEach(e -> validErrors.put(e.getField(), e.getDefaultMessage()));
    return new ResponseEntity<RestResponse<?>>(
        new RestResponse<List<String>>(GeneralResultCodeEnum.VAILD_ERROR, validErrors),
        this.gdoeEveSystemProperties.isAllwayHttpstatusOk()
            ? HttpStatus.OK
            : HttpStatus.BAD_REQUEST);
  }

  @ResponseBody
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<?> handlerNotReadableException(HttpMessageNotReadableException ex) {
    return new ResponseEntity<RestResponse<?>>(
        new RestResponse<Throwable>(GeneralResultCodeEnum.REQUEST_MSG_NOT_READABLE).addObj(ex),
        this.gdoeEveSystemProperties.isAllwayHttpstatusOk()
            ? HttpStatus.OK
            : HttpStatus.BAD_REQUEST);
  }

  /**
   * handlerMethodNotSupportedException
   *
   * <p>405异常处理 <br>
   *
   * @param ex HttpRequestMethodNotSupportedException
   * @return 结果
   */
  @ResponseBody
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<?> handlerMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    return new ResponseEntity<RestResponse<?>>(
        new RestResponse<Throwable>(GeneralResultCodeEnum.REQUEST_METHOD_NOT_SUPPORTED).addObj(ex),
        this.gdoeEveSystemProperties.isAllwayHttpstatusOk()
            ? HttpStatus.OK
            : HttpStatus.METHOD_NOT_ALLOWED);
  }


  /**
   * handlerMediaTypeNotSupportedException
   *
   * <p>415异常处理 <br>
   *
   * @param ex HttpMediaTypeNotSupportedException
   * @return 结果
   */
  @ResponseBody
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<?> handlerMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException ex) {
    return new ResponseEntity<RestResponse<?>>(
        new RestResponse<Throwable>(GeneralResultCodeEnum.REQUEST_MEDIA_NOT_SUPPORTED).addObj(ex),
        this.gdoeEveSystemProperties.isAllwayHttpstatusOk()
            ? HttpStatus.OK
            : HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }


  /**
   * handlerBindException
   *
   * <p>表单验证异常处理 <br>
   *
   * @param ex BindException
   * @return 结果
   */
  @ExceptionHandler(value = BindException.class)
  @ResponseBody
  public ResponseEntity<?> handlerBindException(BindException ex) {
    return new ResponseEntity<RestResponse<?>>(
        new RestResponse<Throwable>(GeneralResultCodeEnum.REQUEST_BIND_ERROR).addObj(ex),
        this.gdoeEveSystemProperties.isAllwayHttpstatusOk()
            ? HttpStatus.OK
            : HttpStatus.BAD_REQUEST);
  }

  @ResponseBody
  @ExceptionHandler({MultiPartParserDefinition.FileTooLargeException.class})
  public void fileTooLargeException(
      MultiPartParserDefinition.FileTooLargeException ex,
      HttpServletRequest request,
      HttpServletResponse respone)
      throws IOException {
    respone.setContentType("application/json");
    respone.setCharacterEncoding("UTF-8");
    PrintWriter out = respone.getWriter();
    out.println(
        JsonMapper.toJsonString(
            new RestResponse<Throwable>(GeneralResultCodeEnum.REQUEST_MULTIPART_ERROR)
                .addObj(ex.getCause())));
    out.close();
    return;
  }

  @ResponseBody
  @ExceptionHandler({RequestTooBigException.class})
  public void requestTooBigException(
      RequestTooBigException ex, HttpServletRequest request, HttpServletResponse respone)
      throws IOException {
    respone.setContentType("application/json");
    respone.setCharacterEncoding("UTF-8");
    PrintWriter out = respone.getWriter();
    out.println(
        JsonMapper.toJsonString(
            new RestResponse<Throwable>(GeneralResultCodeEnum.REQUEST_DATA_TOO_BIG)
                .addObj(ex.getCause())));
    out.close();
    respone.flushBuffer();
  }
}
