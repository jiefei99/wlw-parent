FROM harbor.iqingyu.com.cn/harbor/jike/java8:181

LABEL PROJECT="wlw-server"

WORKDIR /opt/app

ENV LC_ALL en_US.UTF-8
ADD docker-entrypoint.sh /opt/app/
RUN chmod +x /opt/app/docker-entrypoint.sh
ADD wlw-server.jar wlw-server.jar

EXPOSE 8080 8443 9004

CMD ["/opt/app/docker-entrypoint.sh"]
