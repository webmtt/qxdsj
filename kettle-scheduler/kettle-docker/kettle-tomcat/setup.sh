#!/bin/bash

set -e

echo "starting tomcat ...."

if [ ! -d "/usr/simple-jndi/" ];then
  mkdir /usr/simple-jndi/
  echo "mkdir /usr/simple-jndi/"
else
  echo "/usr/simple-jndi/文件夹已经存在"
fi

sh /usr/tomcat/apache-tomcat-9.0.0.M18/bin/startup.sh

tail -f /usr/tomcat/apache-tomcat-9.0.0.M18/logs/catalina.out