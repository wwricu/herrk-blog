#! /bin/bash

mv dao/*.class ../web/WEB-INF/classes/dao > /dev/null
mv ./*.class ../web/WEB-INF/classes > /dev/null

sudo bash ../../../bin/shutdown.sh
sudo bash ../../../bin/startup.sh

exit 0
