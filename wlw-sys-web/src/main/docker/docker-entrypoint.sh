#!/bin/sh
java $JAVA_JAR -server -Xmx${Xmx} -Xms${Xms} \
             -Xss1024k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC \
             -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection  \
             -XX:+UseFastAccessorMethods  \
             -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 \
             -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/apache-tomcat/logs/hs-dump.hprof \
             -XX:ErrorFile=/apache-tomcat/logs/error%p.log \
             -Dfile.encoding=UTF-8  \
             -Dlogging.path=/apache-tomcat/logs \
             -Dlogging.config=classpath:logback.xml \
             -Dspring.profiles.active=prd \
             -jar /opt/app/wlw-sys-web.jar