export REPOSITORY = /home/ec2-user/deploy
export PROJECT_NAME = meowzip

echo "> 현재 구동 중인 애플리케이션 pid 확인"
export CURRENT_PID=$(pgrep -fl meowzip | grep java | awk '{print $1}')

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

export JAR_NAME=$(ls -tr $REPOSITORY/meowzip-api-server-0.0.1-SNAPSHOT.jar | tail -n 1)

echo "> JAR name: $JAR_NAME"
echo "> $JAR_NAME에 실행 권한 추가"
sudo chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

source /home/ec2-user/deploy/env/meowzip.env
echo "$JASYPT_PASSWORD"

nohup java -jar \
    -Dspring.profiles.active=dev \
    -Djasypt.encryptor.password=$JASYPT_PASSWORD \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &