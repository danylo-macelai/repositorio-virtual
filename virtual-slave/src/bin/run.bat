cls
@echo off 

title Virtual Slave

rem Assume que o script está em .../virtual-slave/bin/ e o diretório de trabalho é virtual-slave
cd %~dp0..

SET HEAP_OPTS=-Xms64m -Xmx512m -XX:PermSize=32m -XX:MaxPermSize=128m

SET JMX_OPTS=-Dcom.sun.management.jmxremote.port=9982 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false

SET GC_OPTS=-XX:+UseConcMarkSweepGC -XX:+UseParNewGC

SET DEBUG_OPTS=-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs -XX:ErrorFile=logs/server_java_error%p.log

SET JAVA_OPTS=-server -Dfile.encoding=UTF-8 %GC_OPTS% %HEAP_OPTS% %JMX_OPTS% %DEBUG_OPTS%

SET CONFIG_PATH="%cd%/config/config-slave.properties"

FOR /r %%i IN (virtual-slave-*.jar) DO (
    SET "APP_JAR=%%i"
)

SET MAIN_CLASS="br.com.slave.Application"

if "%1" == "start" (
    @echo start "%2"....
    start "%2" java %JAVA_OPTS% -Dconfiguracoes.path="%CONFIG_PATH%" -cp %APP_JAR% %MAIN_CLASS%
) else if "%1" == "stop" (
    @echo stop %2
    taskkill /fi "WINDOWTITLE eq %2"
) else (
    @echo use "run.bat start" ou "run.bat stop"
)

pause