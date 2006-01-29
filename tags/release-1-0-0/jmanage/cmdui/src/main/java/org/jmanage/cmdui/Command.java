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
import org.jmanage.core.util.Loggers;
import org.jmanage.core.services.AuthService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.services.ServiceContextImpl;
import org.jmanage.core.services.ServiceException;

import org.jmanage.core.auth.UnAuthorizedAccessException;
import org.jmanage.core.util.PasswordField;
import org.jmanage.util.StringUtils;

import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class Command {

    private static final Logger logger = Loggers.getLogger(Command.class);

    private String username;
    private String password;
    private String url;
    private String name;
    private Level logLevel = Level.WARNING; // default log level
    private String[] args;

    static Command get(String line, Command authenticatedCommand)
        throws InvalidCommandException {

        assert authenticatedCommand.username != null &&
                authenticatedCommand.password != null;
        String[] args = toArgs(line);
        Command command = get(args);
        assert command.username == null && command.password == null &&
                command.url == null:"Invalid command.";
        command.username = authenticatedCommand.username;
        command.password = authenticatedCommand.password;
        command.url = authenticatedCommand.url;
        command.logLevel = authenticatedCommand.logLevel;
        return command;
    }

    static Command get(String[] args) throws InvalidCommandException {

        Command command = new Command();
        for(int index=0; index < args.length; index++){
            if(args[index].equals("-username")){
                command.username = args[++index];
            }else if(args[index].equals("-password")){
                command.password = args[++index];
            }else if(args[index].equals("-url")){
                command.url = args[++index];
            }else if(args[index].startsWith("-verbose")){
                int i=args[index].indexOf("=");
                if(i != -1){
                    command.logLevel = Level.parse(args[index].substring(i + 1));
                }else{
                    command.logLevel = Level.FINE; // default verbose level
                }
            }else{
                /* we have the command name and arguments */
                command.name = args[index++];
                CommandHandlerFactory.validateCommand(command.name);
                command.args = new String[args.length - index];
                int argsIndex = 0;
                while(index < args.length){
                    command.args[argsIndex++] = args[index++];
                }
            }
        }
        logger.fine("Command=" + command);
        return command;
    }

    private Command(){}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String[] getArgs() {
        return args;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public boolean isAuthRequired(){
        boolean authRequired = true;
        if(name != null &&
                (name.equals(CommandConstants.HELP) ||
                name.equals(CommandConstants.EXIT))){
            /* no auth required */
            authRequired = false;
        }
        return authRequired;
    }

    public boolean authenticate() throws IOException {

        while(true){
            if(username == null){
                Out.print("Username: ");
                username = In.readLine();
            }

            if(password == null){
                /* get the password */
                password = new String(PasswordField.getPassword("Password:"));
            }

            /* authenticate with the server */
            AuthService authService = ServiceFactory.getAuthService();
            try {
                authService.login(new ServiceContextImpl(), username, password);
                break;
            } catch (ServiceException e){
                Out.println(e.getMessage());
                username = null;
                password = null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    public String toString(){
        StringBuffer buff = new StringBuffer();
        buff.append("username=");
        buff.append(username);
        buff.append(", url=");
        buff.append(url);
        buff.append(", name=");
        buff.append(name);
        buff.append(", args=");
        buff.append(StringUtils.stringArrayToCSV(args));
        return buff.toString();
    }

    public boolean execute() {
        try {
            assert getName() != null;
            CommandHandler handler =
                        CommandHandlerFactory.getHandler(getName());
            boolean result = handler.execute(getHandlerContext());
            Out.println();
            return result;
        } catch (InvalidCommandException e) {
            throw new RuntimeException(e);
        } catch (ServiceException e){
            Out.println(e.getMessage());
            Out.println();
            return false;
        } catch (UnAuthorizedAccessException e){
            Out.println(e.getMessage()+ ", You are not authorized to perform this operation");
            Out.println();
            return false;
        }
    }

    private static String[] toArgs(String str){
        StringTokenizer tokenizer = new StringTokenizer(str, " ");
        String[] args = new String[tokenizer.countTokens()];
        for(int i=0; i<args.length; i++){
            args[i] = tokenizer.nextToken();
        }
        return args;
    }

    private HandlerContext getHandlerContext(){
        return new HandlerContext(this, isAuthRequired());
    }
}
