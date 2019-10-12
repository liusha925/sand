/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/9/24    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.core.service;

import java.lang.reflect.Method;

/**
 * 功能说明：系统日志对外API
 * 开发人员：@author liusha
 * 开发日期：2019/9/24 13:19
 * 功能描述：可以提供给外部使用，内部定义规则，外部去完成实现
 */
public interface ILogBaseService {
  /**
   * 日志初始化
   *
   * @return 自定义日志实体
   */
  Object init();

  /**
   * 横切之前：获取日志信息
   *
   * @param log  自定义日志实体
   * @param args 接收的参数
   */
  void beforeProceed(Object log, Object[] args);

  /**
   * 横切过程：处理异常信息
   *
   * @param log 自定义日志实体
   * @param ep  异常类型
   */
  void exceptionProceed(Object log, Throwable ep);

  /**
   * 横切之后：获取注解信息
   *
   * @param log    自定义日志实体
   * @param method 使用注解的方法
   */
  void afterProceed(Object log, Method method);

  /**
   * 保存日志记录
   *
   * @param log       自定义日志实体
   * @param exeTime   执行时间
   * @param exeStatus 执行状态
   */
  void save(Object log, long exeTime, int exeStatus);
}