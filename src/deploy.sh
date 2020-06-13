#! /bin/bash

javac dao/UserManagerDAO.java util/JwtUtils.java UserManagerServlet.java -cp .:../web/WEB-INF/lib/jackson-core-2.11.0.jar:../web/WEB-INF/lib/jackson-databind-2.11.0.jar:../web/WEB-INF/lib/jackson-annotations-2.11.0.jar:../web/WEB-INF/lib/jjwt-0.7.0.jar:../../../lib/servlet-api.jar

mv dao/*.class ../web/WEB-INF/classes/dao > /dev/null
mv util/*.class ../web/WEB-INF/classes/util > /dev/null
mv ./*.class ../web/WEB-INF/classes > /dev/null

sudo bash ../../../bin/shutdown.sh
sudo bash ../../../bin/startup.sh

exit 0
