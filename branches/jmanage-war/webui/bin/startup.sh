#!/bin/sh
################################################################################
#  Copyright 2004-2005 jManage.org. All rights reserved.
################################################################################

. ./setenv.sh

if [ ! -n "$JMANAGE_CLASSPATH" ]; then
	echo "JMANAGE_CLASSPATH is not set."
	exit 0
fi

$JAVA_HOME/bin/java -ea -classpath $JMANAGE_CLASSPATH $DEBUG_OPTIONS \
    -DJMANAGE_ROOT=$JMANAGE_HOME \
    -Djava.security.policy=java.policy \
    -Djava.util.logging.config.file=$JMANAGE_HOME/logging.properties" \
    org.jmanage.webui.Startup $*
