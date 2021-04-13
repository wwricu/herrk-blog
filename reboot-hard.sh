#!/bin/bash

javac \
    myserver/src/dao/UserManagerDAO.java \
    myserver/src/dao/ArticleManagerDAO.java \
    myserver/src/dao/NumberCountDAO.java \
    myserver/src/dao/InitDAO.java \
    myserver/src/util/JwtUtils.java \
    myserver/src/util/ArticleInfo.java \
    myserver/src/util/UserInfo.java \
    myserver/src/util/Log.java \
    myserver/src/util/NetSpeed.java \
    myserver/src/util/SystemInfo.java \
    myserver/src/util/Daemon.java \
    myserver/src/servlet/ClickCountServlet.java \
    myserver/src/servlet/UserManagerServlet.java \
    myserver/src/servlet/InitServlet.java \
    myserver/src/servlet/ArticleManagerServlet.java \
    myserver/src/servlet/FileManagerServlet.java \
    myserver/src/servlet/TestApiServlet.java \
    myserver/src/servlet/DaemonServlet.java \
    myserver/src/filter/LoginFilter.java \
-cp .:\
myserver/web/WEB-INF/lib/jackson-core-2.11.0.jar:\
myserver/web/WEB-INF/lib/jackson-databind-2.11.0.jar:\
myserver/web/WEB-INF/lib/jackson-annotations-2.11.0.jar:\
myserver/web/WEB-INF/lib/jjwt-0.7.0.jar:\
myserver/web/WEB-INF/lib/mysql-connector-java-8.0.11.jar:\
myserver/lib/servlet-api.jar

mv myserver/src/dao/*.class      myserver/web/WEB-INF/classes/dao > /dev/null
mv myserver/src/util/*.class     myserver/web/WEB-INF/classes/util > /dev/null
mv myserver/src/servlet/*.class  myserver/web/WEB-INF/classes/servlet > /dev/null
mv myserver/src/filter/*.class   myserver/web/WEB-INF/classes/filter > /dev/null

sudo rm ./logs/*
sudo docker stop server
sudo docker rm server
sudo `pwd`/myserver.sh

exit 0