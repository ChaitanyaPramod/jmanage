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
    org.jmanage.webui.Startup $*