#!/bin/sh

if [ ! -n "$JAVA_HOME" ]; then
	echo "Please set JAVA_HOME environment variable. JAVA_HOME must point to a JDK 1.4 installation directory."
    exit 0
fi

if [ ! -n "$JMANAGE_HOME" ]; then
	echo "Please set JMANAGE_HOME environment variable pointing to jManage installation directory."
    exit 0
fi
echo "JMANAGE_HOME is $JMANAGE_HOME"

if [ ! -n "$JMANAGE_LIB_ROOT" ]; then
	JMANAGE_LIB_ROOT=$JMANAGE_HOME
fi

if [ ! -n "$JMANAGE_PORT" ]; then
	JMANAGE_PORT=9090
fi

LIB_JARS=$JMANAGE_LIB_ROOT/lib/javax.servlet.jar:$JMANAGE_LIB_ROOT/lib/org.mortbay.jetty.jar:$JMANAGE_LIB_ROOT/lib/jasper-compiler.jar:$JMANAGE_LIB_ROOT/lib/jasper-runtime.jar:$JMANAGE_LIB_ROOT/lib/ant.jar:$JMANAGE_LIB_ROOT/lib/jdom.jar
EXT_JARS=
JMANAGE_CLASSPATH=$LIB_JARS:$EXT_JARS:$JMANAGE_HOME/classes

$JAVA_HOME/bin/java -ea -classpath $JMANAGE_CLASSPATH $DEBUG_OPTIONS -Djmanage.port=$JMANAGE_PORT -Djmanage.root=$JMANAGE_HOME org.jmanage.webui.Startup
