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
import java.util.Iterator;

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
            CommandHandler handler = null;
            try {
                handler = CommandHandlerFactory.getHandler(args[0]);
                handler.help();
                return true;
            } catch (InvalidCommandException e) {
                Out.println(e.getMessage());
                Out.println();
            }
        }

        /* print short help for all commands */
        Table table = new Table(2);
        Collection commandNames = CommandHandlerFactory.getCommandNames();
        for(Iterator it=commandNames.iterator(); it.hasNext();){
            String commandName = (String)it.next();
            CommandHandler handler =
                    CommandHandlerFactory.getHandler(commandName);
            table.add(commandName, handler.getShortHelp());
        }
        table.print();
        return true;
    }

    public String getShortHelp(){
        return "Prints jManage command line help";
    }

    public void help() {
        Out.println(getShortHelp());
    }
}
