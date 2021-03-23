FROM tomcat
MAINTAINER wwr<thirteenwang@outlook.com>

COPY ./server.xml /usr/local/tomcat/conf/server.xml
COPY ./myserver /usr/local/tomcat/webapps/myserver
COPY ./mysql-connector-java-5.1.6.jar /usr/local/tomcat/lib/mysql-connector-java-5.1.6.jar

ENV CLASSPATH /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar:/usr/local/tomcat/lib/mysql-connector-java-5.1.6.jar

EXPOSE 80 443

# CMD ["/usr/local/tomcat/bin/startup.sh"]
# CMD ["catalina.sh", "run"]
ENTRYPOINT /usr/local/tomcat/bin/catalina.sh run && /usr/local/tomcat/bin/startup.sh