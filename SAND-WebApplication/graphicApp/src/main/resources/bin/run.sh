#!/bin/bash
#
# author:liusha
# description: JAVA APP start|stop|restart|status.

ENV=dev
APP_HOME=`pwd`
APP_NAME=graphicApp
RUNNING_USER=root
LOG_DATE=`date +%Y%m%d%H%M%S`

dirname $0|grep "^/" >/dev/null

#获取APP路径
if [[ $? -eq 0 ]]; then
   APP_HOME=`dirname $0`
else
    dirname $0|grep "^\." >/dev/null:
    temp=$?
    if [[ ${temp} -eq 0 ]]; then
        APP_HOME=`dirname $0|sed "s#^.#$APP_HOME#"`
    else
        APP_HOME=`dirname $0|sed "s#^#$APP_HOME/#"`
    fi
fi

#创建日志路径
if [[ ! -d "$APP_HOME/logs" ]]; then
  mkdir ${APP_HOME}/logs
fi

#实时日志
LOG_PATH=${APP_HOME}/logs/${APP_NAME}.out
#GC日志信息
GC_LOG_PATH=${APP_HOME}/logs/gc-${APP_NAME}-${LOG_DATE}.log
#JMX监控参数
JMX="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1091 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
#JVM启动参数
JVM_OPTS="-Dname=$APP_NAME -Djeesuite.configcenter.profile=$ENV -Duser.timezone=Asia/Shanghai -Xms512M -Xmx512M -XX:PermSize=256M -XX:MaxPermSize=512M -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps -Xloggc:$GC_LOG_PATH -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"

APP_FILE=${APP_NAME}.jar
pid=0

#获取进程号
getPid() {
    pid=`ps -ef |grep ${APP_FILE} |grep -v grep |awk '{print $2}'`
}

#启动服务
start() {
  getPid
  if [[ ! -n "$pid" ]]; then
    JAVA_CMD="nohup java -jar $JVM_OPTS $APP_FILE >> $LOG_PATH 2>&1 &"
    echo "---------------------------------"
    su  ${RUNNING_USER} -c "$JAVA_CMD"
    echo "启动完成，按CTRL+C退出日志界面即可>>>>>"
    echo "---------------------------------"
    sleep 2s
    tail -f ${LOG_PATH}
  else
      echo "$APP_NAME is running PID: $pid"
  fi
}

#停止服务
stop() {
    getPid
    if [[ ! -n "$pid" ]]; then
     echo "$APP_NAME not running"
    else
      echo "$APP_NAME stop..."
      kill -9 ${pid}
    fi
}

#重启服务
restart() {
    stop
    sleep 1s
    start
}

#服务状态
status() {
   getPid
   if [[ ! -n "$pid" ]]; then
     echo "$APP_NAME not running"
   else
     echo "$APP_NAME running PID: $pid"
   fi
}

case $1 in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    *)
        echo "$0: Usage: $0 {start|stop|restart|status}"
        exit 1
        ;;
esac