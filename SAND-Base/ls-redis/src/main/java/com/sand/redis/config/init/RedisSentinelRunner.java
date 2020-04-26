/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/26    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.config.init;

import com.sand.common.util.convert.SandConvert;
import com.sand.common.util.global.Config;
import com.sand.redis.msg.sub.thread.RedisMsgSubThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能说明：Redis哨兵系统
 * 开发人员：@author liusha
 * 开发日期：2020/4/26 16:19
 * 功能描述：哨兵系统搭建参考https://www.cnblogs.com/54hsh/p/12771472.html
 */
@Slf4j
@Component
@Order(value = 10)
public class RedisSentinelRunner implements ApplicationRunner {
  /**
   * Redis消息订阅标识
   */
  public static final String SENTINEL_APPLIED = "__redis_sentinel_applied";

  @Override
  public void run(ApplicationArguments args) throws Exception {
    /**
     * 哨兵系统默认是关闭状态，若要开启，需在启动类上更改代码：
     * <pre>
     *    // 加载自定义配置参数
     *    String[] configs = new String[]{
     *        // 开启Redis哨兵系统
     *        RedisMsgSubRunner.SENTINEL_APPLIED
     *    };
     *    String[] newArgs = org.springframework.util.StringUtils.concatenateStringArrays(args, configs);
     *    SpringApplication.run(LsBackendApplication.class, newArgs);
     * </pre>
     */
    List<String> argsList = Arrays.asList(args.getSourceArgs());
    if (argsList.contains(SENTINEL_APPLIED)) {
      log.info("Redis哨兵系统 启动开始...");
      String channels = Config.getProperty("redis.subscribe.channels");
      String masterName = Config.getProperty("redis.sentinel.masterName", "mymaster");
      int minIdle = SandConvert.obj2Int(Config.getProperty("redis.sentinel.pool.minIdle", "0"));
      int maxIdle = SandConvert.obj2Int(Config.getProperty("redis.sentinel.pool.maxIdle", "8"));
      int maxTotal = SandConvert.obj2Int(Config.getProperty("redis.sentinel.pool.maxTotal", "8"));
      String sentinelAddress = Config.getProperty("redis.sentinel.address", "127.0.0.1:16379,127.0.0.1:26379,127.0.0.1:36379");

      Set<String> sentinels = new HashSet<>();
      Collections.addAll(sentinels, sentinelAddress.split("[,\\s]+"));
      JedisPoolConfig poolConfig = new JedisPoolConfig();
      poolConfig.setMinIdle(minIdle);
      poolConfig.setMaxIdle(maxIdle);
      poolConfig.setMaxTotal(maxTotal);
      log.info("masterName：{}，minIdle：{}，maxIdle：{}，maxTotal：{}，sentinels：{}", masterName, minIdle, maxIdle, maxTotal, sentinels);
      JedisSentinelPool sentinelPool = new JedisSentinelPool(masterName, sentinels, poolConfig);
      // 将哨兵配置信息存储于全局配置config中
      Config.setConfig(SENTINEL_APPLIED, sentinelPool);
      RedisMsgSubThread msgSubThread = new RedisMsgSubThread(sentinelPool, channels);
      msgSubThread.start();
      log.info("...Redis哨兵系统 启动完成");
    } else {
      log.info("Redis哨兵系统 未开启");
    }
  }
}