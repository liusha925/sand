/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/6   liusha      新增
 * =========  ===========  =====================
 */
package com.sand.base.util.lang3;

import com.sand.base.constant.Constant;
import org.apache.commons.lang3.AnnotationUtils;
import org.aspectj.lang.JoinPoint;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 功能说明：注解工具类
 * 开发人员：@author liusha
 * 开发时间：2019/8/6 23:08
 * 功能描述：继承org.apache.commons.lang3.AnnotationUtils类
 */
public class AnnotationUtil extends AnnotationUtils {

  public AnnotationUtil() {
    super();
  }

  /**
   * 获取某个包下被clazz注解的类
   *
   * @param packageName
   * @param clz
   * @return
   */
  public static Set<Class> getAnnotatedClasses(String packageName, Class clz) {
    Reflections reflections = new Reflections(packageName);
    Set<Class> classSet = reflections.getTypesAnnotatedWith(clz);
    return classSet;
  }

  /**
   * 获取某个类下面被annotation注解的方法
   *
   * @param clz
   * @param annotation
   * @return
   */
  public static List<Method> getAnnotatedMethods(Class clz, Class annotation) {
    Method[] methods = clz.getDeclaredMethods();
    List<Method> filterMethods = new ArrayList<>();
    for (Method method : methods) {
      if (!Objects.isNull(method.getAnnotation(annotation))) {
        filterMethods.add(method);
      }
    }
    return filterMethods;
  }

  /**
   * 获取某个类下面被annotation注解的域
   *
   * @param clz
   * @param annotation
   * @return
   */
  public static List<Field> getAnnotatedFields(Class clz, Class annotation) {
    Field[] fields = clz.getDeclaredFields();
    List<Field> filterFields = new ArrayList<>();
    for (Field field : fields) {
      if (!Objects.isNull(field.getAnnotation(annotation))) {
        filterFields.add(field);
      }
    }
    return filterFields;
  }

  /**
   * 获取某个类下面被annotation注解的域的值
   *
   * @param clz
   * @param annotation
   * @return
   */
  public static Map<Field, Object> getAnnotatedFieldsMap(Class clz, Class annotation) {
    Map<Field, Object> filterFields = new HashMap<>();
    Field[] fields = clz.getDeclaredFields();
    for (Field field : fields) {
      Object annotationInstance = field.getAnnotation(annotation);
      if (!Objects.isNull(annotationInstance)) {
        filterFields.put(field, annotationInstance);
      }
    }
    return filterFields;
  }

  /**
   * 获取某些类下面被annotation注解的域的值
   *
   * @param clzSet
   * @param annotation
   * @return
   */
  public static Map<String, Object> getAnnotatedFieldsMap(Collection<Class> clzSet, Class annotation) {
    Map<String, Object> filterFields = new HashMap<>();
    for (Class clz : clzSet) {
      Field[] fields = clz.getDeclaredFields();
      for (Field field : fields) {
        Object annotationInstance = field.getAnnotation(annotation);
        if (!Objects.isNull(annotationInstance)) {
          filterFields.put(clz.getName() + Constant.CLASS_FIELD_SPLIT_SYMBOL + field.getName(), annotationInstance);
        }
      }
    }
    return filterFields;
  }

  /**
   * 获取切面注解信息
   *
   * @param joinPoint
   * @return
   * @throws ClassNotFoundException
   */
  public static Method getAnnotationMethod(JoinPoint joinPoint) throws ClassNotFoundException {
    String targetName = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] arguments = joinPoint.getArgs();
    Class targetClass = Class.forName(targetName);
    Method[] methods = targetClass.getMethods();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        Class[] clzs = method.getParameterTypes();
        if (clzs.length == arguments.length) {
          return method;
        }
      }
    }
    return null;
  }
}