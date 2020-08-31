FROM mysql:5.7.30


# allow no password
ENV MYSQL_ALLOW_EMPTY_PASSWORD yes
ENV LANG=C.UTF-8

#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# copy file into container
COPY setup.sh /mysql/setup.sh
COPY kettle-repository.sql /mysql/kettle-repository.sql
COPY kettle-scheduler.sql /mysql/kettle-scheduler.sql
COPY privileges.sql /mysql/privileges.sql
COPY mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf


#创建tool目录
RUN mkdir /tool
ADD data-integration /tool/

#将宿主机的jdk目录下的文件拷至镜像的/usr/jdk目录下
ADD jdk-8u102-linux-x64.tar.gz /usr/jdk/

#将宿主机的tomcat目录下的文件拷至镜像的/usr/tomcat目录下
ADD apache-tomcat-9.0.0.M18.tar.gz /usr/tomcat/


RUN mkdir /simple-jndi
ADD kettle-scheduler.war /usr/tomcat/apache-tomcat-9.0.0.M18/webapps/

#设置环境变量
ENV JAVA_HOME=/usr/jdk/jdk1.8.0_102
ENV JRE_HOME=$JAVA_HOME/jre
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib:$CLASSPATH
ENV TOMCAT_HOME=/usr/tomcat/apache-tomcat-9.0.0.M18
ENV PATH=/sbin:$JAVA_HOME/bin:$TOMCAT_HOME/bin:$PATH


#公开端口
EXPOSE 8080

# exec these command when container start up
CMD ["sh", "/mysql/setup.sh"]




