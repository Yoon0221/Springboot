#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/demo-0.0.1-SNAPSHOT.jar"  # 실제 JAR 파일 이름으로 대체
APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"
TIME_NOW=$(date +%c)

# 빌드 파일 복사 (파일 경로 확인)
if [ -f "$PROJECT_ROOT/build/libs/demo-0.0.1-SNAPSHOT.jar" ]; then
    echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
    cp "$PROJECT_ROOT/build/libs/demo-0.0.1-SNAPSHOT.jar" $JAR_FILE
else
    echo "$TIME_NOW > 빌드 파일이 존재하지 않습니다. 복사 실패." >> $DEPLOY_LOG
    exit 1
fi

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG