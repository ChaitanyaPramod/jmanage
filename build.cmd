@echo off

set CLASSPATH=./lib/ant/jakarta-oro-2.0.8.jar;./lib/ant/commons-net-1.4.0.jar

ant -f build.xml %*