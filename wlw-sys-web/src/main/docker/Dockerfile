FROM harbor.iqingyu.com.cn/harbor/jike/java8:181

LABEL PROJECT="wlw-sys-web"

WORKDIR /opt/app

ENV LC_ALL en_US.UTF-8
ADD docker-entrypoint.sh /opt/app/
RUN chmod +x /opt/app/docker-entrypoint.sh
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
ADD wlw-sys-web.jar wlw-sys-web.jar

EXPOSE 8080 8443 9004

CMD ["/opt/app/docker-entrypoint.sh"]
