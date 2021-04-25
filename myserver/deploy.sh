#! /bin/bash

javac \
    src/dao/UserManagerDAO.java \
    src/dao/ArticleManagerDAO.java \
    src/dao/NumberCountDAO.java \
    src/dao/ClassManagerDAO.java \
    src/dao/CommentManagerDAO.java \
    src/dao/InitDAO.java \
    src/util/JwtUtils.java \
    src/util/ArticleInfo.java \
    src/util/UserInfo.java \
    src/util/ClassInfo.java \
    src/util/Log.java \
    src/util/NetSpeed.java \
    src/util/SystemInfo.java \
    src/util/CommentInfo.java \
    src/util/Daemon.java \
    src/servlet/ClickCountServlet.java \
    src/servlet/UserManagerServlet.java \
    src/servlet/ClassManagerServlet.java \
    src/servlet/InitServlet.java \
    src/servlet/ArticleManagerServlet.java \
    src/servlet/ArticleViewerServlet.java \
    src/servlet/FileManagerServlet.java \
    src/servlet/TestApiServlet.java \
    src/servlet/DaemonServlet.java \
    src/filter/LoginFilter.java \
-cp .:\
web/WEB-INF/lib/jackson-core-2.11.0.jar:\
web/WEB-INF/lib/jackson-databind-2.11.0.jar:\
web/WEB-INF/lib/jackson-annotations-2.11.0.jar:\
web/WEB-INF/lib/jjwt-0.7.0.jar:\
web/WEB-INF/lib/mysql-connector-java-8.0.11.jar:\
lib/servlet-api.jar

mv src/dao/*.class      web/WEB-INF/classes/dao > /dev/null
mv src/util/*.class     web/WEB-INF/classes/util > /dev/null
mv src/servlet/*.class  web/WEB-INF/classes/servlet > /dev/null
mv src/filter/*.class   web/WEB-INF/classes/filter > /dev/null

exit 0
