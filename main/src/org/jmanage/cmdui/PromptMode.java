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

import org.jmanage.cmdui.util.In;
import org.jmanage.cmdui.util.Out;

import java.io.IOException;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class PromptMode implements CommandConstants {

    private final Command authenticatedCommand;

    PromptMode(Command command){
        this.authenticatedCommand = command;
    }

    public void start()
        throws IOException {

        while(true){
            try {
                /* get input */
                Command command = getCommand();
                /* execute command */
                command.execute();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private Command getCommand()
        throws IOException, InvalidCommandException {
        String line = readLine();
        return Command.get(line, authenticatedCommand);
    }

    private String readLine() throws IOException{

        String line = null;
        while(line == null || line.trim().length() == 0){
            Out.print("jmanage>");
            line = In.readLine();
        }
        return line;
    }

    private void handleException(Exception e){
        if(e instanceof InvalidCommandException){
            Out.println(e.getMessage());
        }else{
            e.printStackTrace();
        }
    }
}
