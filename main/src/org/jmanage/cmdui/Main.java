/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.cmdui;

import org.jmanage.core.services.ServiceFactory;

import java.io.IOException;

/**
 *
 * jmanage -username admin -password 123456 -url <jmanage-url> <command> <command args>
 *
 * commands:
 *
 * listApps
 * listMBeans <appName> [filter expression]
 * view       <appName>/<mbeanName [configured name or object name]>
 * view       <appName>/<mbeanName>/[attributeName1|attributeName2|attributeName3]
 * execute    <appName>/<mbeanName>/<operationName> [args]
 * modify     <appName>/<mbeanName>/<attributeName> newValue
 * printValue <appName>/<mbeanName>/[attributeName1|attributeName2|attributeName3]
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class Main {

    static{
        /* initialize ServiceFactory */
        ServiceFactory.init(ServiceFactory.MODE_REMOTE);
    }

    public static void main(String[] args)
        throws Exception {

        Command command = Command.get(args);

        /* authenticate the user */
        if(command.isAuthRequired()){
            if(!command.authenticate()){
                System.out.println("Authentication failed.");
                return;
            }
        }

        if(command.getName() == null){
            /* get into the prompt mode */
            PromptMode promptMode = new PromptMode(command);
            promptMode.start();
        }else{
            /* execute command */
            command.execute();
        }
    }
}
