#!/bin/bash

BACKUP=/media/disk/docker/mysql_bak

DATETIME=$(date +%Y_%m_%d_%H%M%S)

echo $DATETIME

echo "=================开始备份================"

echo "=================备份的路径是$BACKUP/$DATETIME.sql.gz"

HOST=localhost

DATABASE=penguin

DB_USER=root

DB_PWD=123456

mkdir -p "$BACKUP"

docker exec 6de5ec36a47e mysqldump -u$DB_USER -p$DB_PWD --host=$HOST $DATABASE | gzip >$BACKUP/$DATETIME.sql.gz

echo "dump is done"
