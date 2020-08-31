use mysql;
-- new user
set password for root@localhost = password('123456');
-- important
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
-- use privileges
FLUSH PRIVILEGES;

