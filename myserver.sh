#! /bin/bash
TOMCAT=/usr/local/tomcat

sudo docker run \
    -v `pwd`/myserver:$TOMCAT/webapps/myserver \
    -v `pwd`/conf:$TOMCAT/conf \
    -v `pwd`/logs:$TOMCAT/logs \
    -v `pwd`/mysql-connector-java-5.1.6.jar:$TOMCAT/lib/mysql-connector-java-5.1.6.jar \
    -e CLASSPATH=$TOMCAT/bin/bootstrap.jar:$TOMCAT/bin/tomcat-juli.jar:$TOMCAT/lib/mysql-connector-java-5.1.6.jar \
    -p 80:8080 -p 443:443 -d \
    --name server --restart=always tomcat:latest
