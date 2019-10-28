/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能说明：正则表达式枚举类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 8:40
 * 功能描述：正则表达式枚举类
 */
@Getter
@AllArgsConstructor
public enum RegexEnum {
  // 系统规则
  PASSWORD("^[A-Za-z0-9]{8,16}$", "登录密码"),
  PAY_PASSWORD("^[A-Za-z0-9]{4,10}$", "交易密码"),
  LOGIN_NAME("^[A-Za-z0-9]{8,16}$", "登录用户名"),
  // 要素规则
  BANK_NUMBER("^[1-9]{1}[0-9]{7,22}$", "银行卡号"),
  REAL_NAME("^([\\u4e00-\\u9fa5]){2,4}$", "真实姓名"),
  MOBILE_NUMBER("^((13[0-9])|(14[5,7,9])|(15([0-9]))|(16[6,7,8,9])|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))[0-9]{8}$", "手机号码"),
  ID_NUMBER("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)", "身份证号"),
  // 通用规则
  INTEGER("\\d*", "整数"),
  NUMBER("^[0-9]*.[0-9]*$", "数字"),
  DECIMAL("^([0-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])?$", "小数"),
  EMAIL("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", "电子邮箱"),
  TEL_NUMBER("^[1-9]{1}[0-9]{5,8}$", "电话号码"),
  AREA_TEL_NUMBER("^[0][1-9]{2,3}-[1-9]{1}[0-9]{5,8}$", "电话号码-带区号"),
  UNIFY_CODE("^([0-9ABCDEFGHJKLMNPQRTUWXY]{2})([0-9]{6})([0-9ABCDEFGHJKLMNPQRTUWXY]{10})$", "统一社会信用代码"),
  DATE("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$", "日期格式");

  private final String expression;
  private final String description;
}
