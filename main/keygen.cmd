@rem TODO: need to ensure that JMANAGE_HOME and JAVA_HOME are set

if "%JAVA_HOME%" == "" set JAVA_HOME=:\j2sdk1.4.2_04
set JMANAGE_PORT=9090
set JMANAGE_ROOT=./build

set JMANAGE_CLASSPATH=%JMANAGE_ROOT%/classes

java -classpath %JMANAGE_CLASSPATH% -Djmanage.root=%JMANAGE_ROOT% org.jmanage.core.crypto.EncryptedKeyGenerator