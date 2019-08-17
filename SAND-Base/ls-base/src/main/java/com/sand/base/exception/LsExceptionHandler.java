/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   nevercoming   新增
 * =========  ===========  =====================
 */
package com.sand.base.exception;

import com.sand.base.Ret;
import com.sand.base.enums.RetEnum;
import com.sand.base.util.RetUtil;
import com.sand.base.util.common.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 功能说明：全局异常处理类
 * 开发人员：@author nevercoming
 * 开发日期：2019/8/16 13:21
 * 功能描述：全局异常处理类
 */
@Slf4j
@Order(101)
@ResponseBody
@ControllerAdvice
public class LsExceptionHandler {

  /**
   * 顶级的异常处理
   *
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  public Ret handleException(Exception e) {
    if (e.getCause() instanceof LsException) {
      return handleLsException((LsException) e.getCause());
    }
    errorLog(e);
    log.error("错误信息：{}", e);
    return RetUtil.error();
  }

  /**
   * 自定义异常处理
   *
   * @param e
   * @return
   */
  @ExceptionHandler(LsException.class)
  public Ret handleLsException(LsException e) {
    errorLog(e);
    return RetUtil.result(null, e.getCode(), e.getMessage());
  }

  /**
   * 参数缺失处理
   *
   * @param e
   * @return
   */
  @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
  public Ret handleMissingParamException(Exception e) {
    errorLog(e);
    return RetUtil.error(RetEnum.PARAM_MISSING_ERROR);
  }

  /**
   * 打印出错log
   *
   * @param e
   */
  protected void errorLog(Exception e) {
    StackTraceElement element = e.getStackTrace()[0];
    if (e instanceof LsException) {
      log.info("异常位置：{}.{}，第{}行，原因：{}", element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getMessage());
      if (!Objects.isNull(e.getCause())) {
        StackTraceElement cause = e.getCause().getStackTrace()[0];
        log.info("起因：{}.{}，第{}行，原因：{}", cause.getClassName(), cause.getMethodName(), cause.getLineNumber(), e.getCause().getMessage());
      }
    } else {
      log.error("错误位置：{}.{}，第{}行，错误原因：{}", element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getClass().getName());
    }
  }

  /**
   * 通用异常之外
   *
   * @param retEnum
   * @throws IOException
   */
  public static void commonException(RetEnum retEnum) {
    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    HttpMessageConverter httpMessageConverter = SpringUtil.getBean("httpMessageConverter", HttpMessageConverter.class);
    try {
      httpMessageConverter.write(RetUtil.error(retEnum), MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
    } catch (IOException e) {
      log.error("系统异常", e);
    }
  }
}
