################################################################################
#  Copyright 2004-2005 jManage.org
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
################################################################################
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
