#!/bin/sh

if [ ! -n "$JAVA_HOME" ]; then
	echo "Please set JAVA_HOME environment variable. JAVA_HOME must point to a JDK 1.4 installation directory."
    exit 0
fi

if [ ! -n "$JMANAGE_HOME" ]; then
	echo "Please set JMANAGE_HOME environment variable pointing to jManage installation directory."
    exit 0
fi

if [ ! -n "$JMANAGE_LIB_ROOT" ]; then
	JMANAGE_LIB_ROOT=$JMANAGE_HOME
fi

JMANAGE_CLASSPATH=$JMANAGE_HOME/classes:$JMANAGE_LIB_ROOT/lib/jdom.jar

$JAVA_HOME/bin/java -classpath $JMANAGE_CLASSPATH -Djmanage.root=$JMANAGE_HOME org.jmanage.core.tools.ChangeAdminPassword
