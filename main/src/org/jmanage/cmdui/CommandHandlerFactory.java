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

import org.jmanage.cmdui.commands.ListApplicationsHandler;
import org.jmanage.cmdui.commands.NoOpHandler;
import org.jmanage.cmdui.commands.HelpHandler;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
class CommandHandlerFactory implements CommandConstants {

    private static Map commandNameToInstanceMap = new HashMap();

    static{
        commandNameToInstanceMap.put(LIST_APPS, new ListApplicationsHandler());
        commandNameToInstanceMap.put(HELP, new HelpHandler());
        commandNameToInstanceMap.put(EXIT, new NoOpHandler());
    }

    static CommandHandler getHandler(String commandName)
        throws InvalidCommandException {

        CommandHandler handler =
                (CommandHandler)commandNameToInstanceMap.get(commandName);
        if(handler == null){
            throw new InvalidCommandException(commandName);
        }
        return handler;
    }

    static void validateCommand(String commandName)
        throws InvalidCommandException {

        if(!commandNameToInstanceMap.containsKey(commandName)){
            throw new InvalidCommandException(commandName);
        }
    }
}
