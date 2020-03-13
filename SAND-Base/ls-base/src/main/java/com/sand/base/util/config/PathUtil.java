/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/13    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util.config;

import com.sand.base.util.lang3.StringUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * 功能说明：获取文件路径
 * 开发人员：@author liusha
 * 开发日期：2020/3/13 10:03
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Slf4j
@NoArgsConstructor
public class PathUtil {

  /**
   * 获取文件路径
   *
   * @param obj 对象
   * @return obj路径
   */
  public static String getPath(Object obj) {
    return getPath(obj.getClass());
  }

  /**
   * 获取文件路径
   *
   * @param clz class
   * @return class路径
   */
  public static String getPath(Class<?> clz) {
    String path = clz.getResource(StringUtil.EMPTY).getPath();
    return new File(path).getAbsolutePath();
  }

  /**
   * 获取对象包的路径
   *
   * @param obj 对象
   * @return package路径
   */
  public static String getPackagePath(Object obj) {
    return getPackagePath(obj.getClass());
  }

  /**
   * 获取对象包的路径
   *
   * @param clz class
   * @return package路径
   */
  public static String getPackagePath(Class<?> clz) {
    Package p = clz.getPackage();
    return Objects.isNull(p) ? StringUtil.EMPTY : p.getName().replaceAll("\\.", "/");
  }

  /**
   * 获取ClassLoader
   *
   * @return ClassLoader
   */
  public static ClassLoader getClassLoader() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    return Objects.nonNull(classLoader) ? classLoader : PathUtil.class.getClassLoader();
  }

}
