/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2019/8/16   liusha   新增
 * =========  ===========  =====================
 */
package com.sand.base.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sand.base.enums.CodeEnum;
import com.sand.base.exception.LsException;
import com.sand.base.util.lang3.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 功能说明：参数获取工具类
 * 开发人员：@author liusha
 * 开发日期：2019/8/16 14:29
 * 功能描述：参数获取工具类
 */
public class ParamUtil {
  /**
   * 默认整型参数值
   */
  private static final int DEFAULT_PARAM_INT = 0;
  /**
   * 默认分页条数
   */
  private static final int DEFAULT_PAGE_SIZE = 10;
  /**
   * 默认当前页面
   */
  private static final int DEFAULT_PAGE_CURRENT = 1;
  /**
   * 默认分页最大条数
   **/
  private static final int DEFAULT_MAX_PAGE_SIZE = 100;

  public ParamUtil() {
  }

  /**
   * 获取整型参数（非必须），默认DEFAULT_INT_PARAM = 0
   *
   * @param map
   * @param key
   * @return
   */
  public static int getIntValue(Map<String, Object> map, String key) {
    return getIntValue(map, key, DEFAULT_PARAM_INT);
  }

  /**
   * 获取整型参数（非必须），默认defaultValue
   *
   * @param map
   * @param key
   * @param defaultValue
   * @return
   */
  public static int getIntValue(Map<String, Object> map, String key, int defaultValue) {
    String value = StringUtil.obj2Str(getValue(map, key, defaultValue));
    if (StringUtil.isBlank(value)) {
      return defaultValue;
    }
    return Integer.parseInt(getValue(map, key, defaultValue).toString());
  }

  /**
   * 获取整型参数
   *
   * @param map
   * @param key
   * @param required
   * @return
   */
  public static int getIntValue(Map<String, Object> map, String key, boolean required) {
    return Integer.parseInt(getValue(map, key, DEFAULT_PARAM_INT, required).toString());
  }

  /**
   * 获取字符串参数值（必须）
   *
   * @param map
   * @param key
   * @return
   */
  public static String getStringValue(Map<String, Object> map, String key) {
    Object value = getValue(map, key, null, true);
    return Objects.isNull(value) ? null : StringUtils.deleteWhitespace(value.toString());
  }

  /**
   * 获取字符串参数信息（非必须）
   *
   * @param map
   * @param key
   * @param defaultValue
   * @return
   */
  public static String getStringValue(Map<String, Object> map, String key, Object defaultValue) {
    Object value = getValue(map, key, defaultValue, false);
    return Objects.isNull(value) ? null : StringUtils.deleteWhitespace(value.toString());
  }

  /**
   * 获取BigDecimal参数值（必须）
   *
   * @param map
   * @param key
   * @return
   */
  public static BigDecimal getBigDecimalValue(Map<String, Object> map, String key) {
    Object value = getValue(map, key, null, true);
    return Objects.isNull(value) ? null : BigDecimal.valueOf(Double.parseDouble(value.toString()));
  }

  /**
   * 获取BigDecimal参数值（非必须）
   *
   * @param map
   * @param key
   * @param defaultValue
   * @return
   */
  public static BigDecimal getBigDecimalValue(Map<String, Object> map, String key, BigDecimal defaultValue) {
    Object value = getValue(map, key, defaultValue, false);
    return Objects.isNull(value) ? BigDecimal.ZERO : BigDecimal.valueOf(Double.parseDouble(value.toString()));
  }

  /**
   * 长整型
   *
   * @param map
   * @param key
   * @param defaultValue
   * @return
   */
  public static long getLongValue(Map<String, Object> map, String key, long defaultValue) {
    Object value = getValue(map, key, defaultValue, false);
    return Objects.isNull(value) ? 0L : Long.valueOf(value + "");
  }

  /**
   * 对象
   *
   * @param map
   * @param key
   * @param clazz
   * @param <K>
   * @return
   */
  public static <K> K getObjectValue(Map<String, Object> map, String key, Class<K> clazz) {
    String value = getStringValue(map, key, null);
    K k;
    try {
      if (StringUtil.isNotBlank(value)) {
        k = JSONObject.parseObject(value, clazz);
      } else {
        k = null;
      }
    } catch (Exception e) {
      throw new LsException(CodeEnum.PARAM_CHECKED_ERROR, e);
    }
    return k;
  }

  /**
   * 转换带泛型的对象
   *
   * @param map
   * @param key
   * @param type
   * @return
   */
  public static Object getObjectValue(Map<String, Object> map, String key, TypeReference type) {
    String value = getStringValue(map, key, null);
    Object obj = null;
    try {
      if (StringUtil.isNotBlank(value)) {
        obj = JSONObject.parseObject(value, type);
      }
    } catch (Exception e) {
      throw new LsException(CodeEnum.PARAM_CHECKED_ERROR, e);
    }
    return obj;
  }

  /**
   * 获取参数值（必须）
   *
   * @param map
   * @param key
   * @return
   */
  public static Object getValue(Map<String, Object> map, String key) {
    return getValue(map, key, null, true);
  }

  /**
   * 获取参数信息（非必须）
   *
   * @param map
   * @param key
   * @param defaultValue
   * @return
   */
  public static Object getValue(Map<String, Object> map, String key, Object defaultValue) {
    return getValue(map, key, defaultValue, false);
  }

  /**
   * 获取参数信息
   *
   * @param map
   * @param key
   * @param defaultValue
   * @param required
   * @return
   */
  public static Object getValue(Map<String, Object> map, String key, Object defaultValue, boolean required) {
    if (Objects.isNull(map)) {
      if (required) {
        throw new LsException(CodeEnum.PARAM_MISSING_ERROR, "参数【" + key + "】不能为空");
      }
      return defaultValue;
    }
    if (map.containsKey(key)) {
      Object value = map.get(key);
      if (required && StringUtil.isBlank(StringUtil.obj2Str(value))) {
        throw new LsException(CodeEnum.PARAM_MISSING_ERROR, "参数【" + key + "】不能为空");
      }
      return value;
    } else if (required) {
      throw new LsException(CodeEnum.PARAM_MISSING_ERROR, "参数【" + key + "】不能为空");
    }
    return defaultValue;
  }

  /**
   * 获取分页参数，默认一页10
   *
   * @param entity
   * @return
   */
  public static Page pageParam(Object entity) {
    return pageParam(ConvertUtil.obj2Map(entity), DEFAULT_PAGE_SIZE);
  }

  /**
   * 获取分页参数，默认一页10
   *
   * @param map
   * @return
   */
  public static Page pageParam(Map<String, Object> map) {
    return pageParam(map, DEFAULT_PAGE_SIZE);
  }

  /**
   * 获取分页参数
   *
   * @param map
   * @param defaultSize
   * @return
   */
  public static Page pageParam(Map<String, Object> map, int defaultSize) {
    return pageParam(map, defaultSize, DEFAULT_MAX_PAGE_SIZE);
  }

  /**
   * 获取分页参数
   *
   * @param map
   * @param defaultSize
   * @param maxSize
   * @return
   */
  public static Page pageParam(Map<String, Object> map, int defaultSize, int maxSize) {
    if (Objects.isNull(map)) {
      map = new HashMap<>();
    }
    // 每页条数
    int size;
    String pageSize = StringUtil.obj2Str(map.get("size"));
    if (StringUtil.isBlank(pageSize)) {
      size = DEFAULT_PAGE_SIZE;
    } else {
      size = getIntValue(map, "size", defaultSize);
    }
    // 当前页码
    int current;
    String pageCurrent = StringUtil.obj2Str(map.get("current"));
    if (StringUtil.isBlank(pageCurrent)) {
      current = DEFAULT_PAGE_CURRENT;
    } else {
      current = getIntValue(map, "current", DEFAULT_PAGE_CURRENT);
    }
    size = Math.min(size, maxSize);
    Page page = new Page(current, size);
    return page;
  }

}
