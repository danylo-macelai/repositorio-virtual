#!/bin/bash

echo -e "Virtual Slave"

# Assume que o script está em /virtual-slave/bin/ e o diretório de trabalho é /virtual-slave

cd ..

export HEAP_OPTS="-Xms64m -Xmx512m -XX:PermSize=32m -XX:MaxPermSize=128m"

export JMX_OPTS="-Dcom.sun.management.jmxremote.port=9982 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"

export GC_OPTS="-XX:+UseConcMarkSweepGC -XX:+UseParNewGC"

export DEBUG_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs -XX:ErrorFile=logs/server_java_error_$(date +'%Y-%m-%d-%H%M%S').log"

export JAVA_OPTS="-server -Dfile.encoding=UTF-8 $GC_OPTS $HEAP_OPTS $JMX_OPTS $DEBUG_OPTS"

export CONFIG_PATH="$(pwd)/config/config-slave.properties"

export APP_JAR=""

for i in virtual-slave-*.jar
do
    APP_JAR="$APP_JAR:$i";
done

export MAIN_CLASS="br.com.slave.Application"

if [ $1 == "start" ]
then
    echo "start $2..."
    java $JAVA_OPTS -Dconfiguracoes.path=$CONFIG_PATH -cp $APP_JAR $MAIN_CLASS
elif [ $1 == "stop" ]
then
    echo "stop $2..."
    pkill -f $2
else
    echo "use run.sh start ou run.sh stop"
fi
