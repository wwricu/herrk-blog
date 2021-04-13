#! /bin/bash
TOMCAT=/usr/local/tomcat
docker run \
    -v `pwd`/myserver:$TOMCAT/webapps/myserver \
    -v `pwd`/conf:$TOMCAT/conf \
    -v `pwd`/logs:$TOMCAT/logs \
    -v /proc:/home/proc \
    -v `pwd`/lib/mysql-connector-java-8.0.16.jar:$TOMCAT/lib/mysql-connector-java-8.0.16.jar \
    -e CLASSPATH=/usr/local/tomcat/lib/mysql-connector-java-8.0.16.jar \
    --net=host -d --name server --restart=always tomcat:latest
