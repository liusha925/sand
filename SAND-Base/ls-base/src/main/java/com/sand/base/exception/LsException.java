/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.exception;

import com.sand.base.enums.CodeEnum;
import lombok.Data;

/**
 * 功能说明：自定义异常类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 10:32
 * 功能描述：自定义异常类
 */
@Data
public class LsException extends RuntimeException {
  private int code;

  public LsException() {
    super(CodeEnum.ERROR.getMsg());
    this.code = CodeEnum.ERROR.getCode();
  }

  public LsException(CodeEnum codeEnum) {
    super(codeEnum.getMsg());
    this.code = codeEnum.getCode();
  }

  public LsException(CodeEnum codeEnum, String msg) {
    super(msg);
    this.code = codeEnum.getCode();
  }

  public LsException(String msg) {
    super(msg);
    this.code = CodeEnum.ERROR.getCode();
  }

  public LsException(int code, String msg) {
    super(msg);
    this.code = code;
  }

  public LsException(String msg, Exception e) {
    super(msg, e);
    this.code = CodeEnum.ERROR.getCode();
  }

  public LsException(CodeEnum codeEnum, Exception e) {
    super(codeEnum.getMsg(), e);
    this.code = codeEnum.getCode();
  }

  public LsException(CodeEnum codeEnum, String msg, Exception e) {
    super(msg, e);
    this.code = codeEnum.getCode();
  }

  public LsException(int code, String msg, Exception e) {
    super(msg, e);
    this.code = code;
  }
}
