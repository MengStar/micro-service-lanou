FROM mysql:5.7
#设置登录密码
ENV MYSQL_ROOT_PASSWORD password
#暴露的端口
EXPOSE 3306
#启动
RUN service mysql start &&\
    mysql -e "grant all privileges on *.* to 'root'@'%' identified by 'password' WITH GRANT OPTION ;"&&\
    mysql -e "grant all privileges on *.* to 'root'@'localhost' identified by 'password' WITH GRANT OPTION ;"



