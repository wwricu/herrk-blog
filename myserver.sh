#! /bin/bash
TOMCAT=/usr/local/tomcat

sudo docker run \
    -v `pwd`/myserver:$TOMCAT/webapps/myserver \
    -v `pwd`/conf:$TOMCAT/conf \
    -v `pwd`/logs:$TOMCAT/logs \
    -v `pwd`/lib/mysql-connector-java-8.0.16.jar:$TOMCAT/lib/mysql-connector-java-8.0.16.jar \
    -e CLASSPATH=/usr/local/tomcat/lib/mysql-connector-java-8.0.16.jar \
    -p 80:8080 -p 443:443 -d \
    --name server --restart=always tomcat:latest
