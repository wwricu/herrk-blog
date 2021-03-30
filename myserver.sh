#! /bin/bash
TOMCAT=/usr/local/tomcat

sudo docker run \
    -v $PWD/myserver:$TOMCAT/webapps/myserver \
    -v $PWD/conf:$TOMCAT/conf \
    -v $PWD/logs:$TOMCAT/logs \
    -v $PWD/mysql-connector-java-5.1.6.jar:$TOMCAT/lib/mysql-connector-java-5.1.6.jar \
    -e CLASSPATH=$TOMCAT/bin/bootstrap.jar:$TOMCAT/bin/tomcat-juli.jar:$TOMCAT/lib/mysql-connector-java-5.1.6.jar \
    -p 80:8080 -p 443:443 -itd \
    --name server --restart=always tomcat:latest
