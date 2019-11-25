/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/11/20    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.exception.handler;

import com.sand.base.core.controller.BaseController;
import com.sand.base.core.entity.ResultEntity;
import com.sand.base.util.ResultUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 功能说明：全局异常处理
 * 开发人员：@author liusha
 * 开发日期：2019/11/20 16:54
 * 功能描述：全局异常处理，继承自BaseController
 */
@RestControllerAdvice
public class LsExceptionHandler extends BaseController {
  /**
   * 顶级异常处理
   *
   * @param e 异常
   * @return 响应客户端
   */
  @ExceptionHandler(Throwable.class)
  public ResultEntity handleThrowable(Throwable e) {
    super.errorLog(e);
    return ResultUtil.error();
  }
}
