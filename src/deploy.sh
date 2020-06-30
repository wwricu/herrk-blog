#! /bin/bash

javac dao/UserManagerDAO.java dao/ArticleManagerDAO.java dao/NumberCountDAO.java dao/InitDAO.java \
util/JwtUtils.java util/ArticleInfo.java \
servlet/ClickCountServlet.java servlet/UserManagerServlet.java servlet/InitServlet.java servlet/ArticleManagerServlet.java servlet/TestApiServlet.java \
filter/LoginFilter.java \
-cp .:../web/WEB-INF/lib/jackson-core-2.11.0.jar:../web/WEB-INF/lib/jackson-databind-2.11.0.jar:../web/WEB-INF/lib/jackson-annotations-2.11.0.jar:../web/WEB-INF/lib/jjwt-0.7.0.jar:../../../lib/servlet-api.jar

mv dao/*.class ../web/WEB-INF/classes/dao > /dev/null
mv util/*.class ../web/WEB-INF/classes/util > /dev/null
mv servlet/*.class ../web/WEB-INF/classes/servlet > /dev/null
mv filter/*.class ../web/WEB-INF/classes/filter > /dev/null

sudo bash ../../../bin/shutdown.sh
sudo bash ../../../bin/startup.sh

exit 0
