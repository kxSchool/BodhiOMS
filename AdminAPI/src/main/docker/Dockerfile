# 该镜像需要依赖的基础镜像
FROM java:8
VOLUME /tmp
# 将当前目录下的jar包复制到docker容器的/目录下
ADD Penguin.jar Penguin.jar
# 运行过程中创建一个mall-tiny-docker-file.jar文件
RUN bash -c 'touch /tmp/Penguin.jar'
# 声明服务运行在8080端口
EXPOSE 9999
# 指定docker容器启动时运行jar包
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","Penguin.jar"]
# 指定维护者的名字
MAINTAINER 28586585 <28586585@qq.com>