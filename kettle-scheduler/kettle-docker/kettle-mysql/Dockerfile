FROM mysql:5.7.30

# allow no password
ENV MYSQL_ALLOW_EMPTY_PASSWORD yes
ENV LANG=C.UTF-8

RUN mkdir /home/mysql

# copy file into container
COPY setup.sh /mysql/setup.sh
COPY kettle-repository.sql /mysql/kettle-repository.sql
COPY kettle-scheduler.sql /mysql/kettle-scheduler.sql
COPY privileges.sql /mysql/privileges.sql
COPY mysqld.cnf /etc/mysql/mysql.conf.d/mysqld.cnf



# exec these command when container start up
CMD ["sh", "/mysql/setup.sh"]




