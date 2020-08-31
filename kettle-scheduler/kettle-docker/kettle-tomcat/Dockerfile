FROM centos:centos7
#MAINTAINER xxx@qq.com

#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#设置系统编码
RUN yum install kde-l10n-Chinese -y
RUN yum install glibc-common -y
RUN localedef -c -f UTF-8 -i zh_CN zh_CN.utf8
#RUN export LANG=zh_CN.UTF-8
#RUN echo "export LANG=zh_CN.UTF-8" >> /etc/locale.conf
#ENV LANG zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8

#切换镜像目录，进入/usr目录
WORKDIR /usr
#在/usr/下创建jdk目录,用来存放jdk文件
RUN mkdir jdk
#在/usr/下创建tomcat目录，用来存放tomcat
RUN mkdir tomcat
#创建tool目录
RUN mkdir /tool
ADD data-integration /tool/
COPY setup.sh /usr/setup.sh

#将宿主机的jdk目录下的文件拷至镜像的/usr/jdk目录下
ADD jdk-8u102-linux-x64.tar.gz /usr/jdk/

#将宿主机的tomcat目录下的文件拷至镜像的/usr/tomcat目录下
ADD apache-tomcat-9.0.0.M18.tar.gz /usr/tomcat/


RUN mkdir /usr/tomcat/apache-tomcat-9.0.0.M18/bin/simple-jndi
ADD kettle-scheduler.war /usr/tomcat/apache-tomcat-9.0.0.M18/webapps/

#设置环境变量
ENV JAVA_HOME=/usr/jdk/jdk1.8.0_102
ENV JRE_HOME=$JAVA_HOME/jre
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib:$CLASSPATH
ENV TOMCAT_HOME=/usr/tomcat/apache-tomcat-9.0.0.M18
ENV PATH=/sbin:$JAVA_HOME/bin:$TOMCAT_HOME/bin:$PATH


#公开端口
EXPOSE 8080
#设置启动命令
#ENTRYPOINT ["/usr/tomcat/apache-tomcat-9.0.0.M18/bin/catalina.sh","run"]
#CMD ["/usr/tomcat/apache-tomcat-9.0.0.M18/bin/catalina.sh","run"]
CMD ["sh", "/usr/setup.sh"]
