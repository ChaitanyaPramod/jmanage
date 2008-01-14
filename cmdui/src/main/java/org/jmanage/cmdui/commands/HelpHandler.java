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
package org.jmanage.cmdui.commands;

import org.jmanage.cmdui.*;
import org.jmanage.cmdui.util.Out;
import org.jmanage.cmdui.util.Table;

import java.util.Collection;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class HelpHandler implements CommandHandler {

    /**
     *
     * @param context
     * @return true if the command was handled properly; false otherwise
     */
    public boolean execute(HandlerContext context) {

        try {
            return execute0(context);
        } catch (InvalidCommandException e) {
            /* this is not possible */
            throw new RuntimeException(e);
        }
    }

    private boolean execute0(HandlerContext context)
        throws InvalidCommandException {

        String[] args = context.getCommand().getArgs();
        if(args.length == 1){
            /* print long help for given command */
            CommandHandler handler;
            try {
                handler = CommandHandlerFactory.getHandler(args[0]);
                handler.help();
                return true;
            } catch (InvalidCommandException e) {
                Out.println(e.getMessage());
                Out.println();
            }
        }

        /* print help about using jmanage command */
        Out.println("jmanage [-username <username>] [-password <password>] " +
                "[-verbose[=<level>]] [command] [command args]");
        /* print short help for all commands */
        Out.println();
        Out.println("Commands:");
        Table table = new Table(2);
        Collection commandNames = CommandHandlerFactory.getCommandNames();
        for (Object commandName1 : commandNames) {
            String commandName = (String) commandName1;
            CommandHandler handler =
                    CommandHandlerFactory.getHandler(commandName);
            table.add(commandName, handler.getShortHelp());
        }
        table.print();
        Out.println();
        Out.println("Type \"help <command>\" for detailed command help.");
        Out.println("[Important! : Any space within a command argument should be replaced with \"~\" character.]");
        return true;
    }

    public String getShortHelp(){
        return "Prints jManage command line help";
    }

    public void help() {
        Out.println(getShortHelp());
    }
}
