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

import org.jmanage.cmdui.CommandHandler;
import org.jmanage.cmdui.HandlerContext;
import org.jmanage.cmdui.CommandConstants;
import org.jmanage.cmdui.util.Out;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.data.OperationResultData;

/**
 *
 * date:  Feb 26, 2005
 * @author	Rakesh Kalra
 */
public class ExecuteHandler implements CommandHandler {

    /**
     *
     * @param context
     * @return true if the command was handled properly; false otherwise
     */
    public boolean execute(HandlerContext context) {

        String[] args = context.getCommand().getArgs();
        if(args.length == 0){
            usage();
            return false;
        }
        Expression expression = new Expression(args[0]);
        if(expression.getAppName() == null ||
                expression.getMBeanName() == null ||
                expression.getTargetName() == null){
            usage();
            return false;
        }
        String[] params = new String[args.length - 1];
        for(int i=0; i<params.length; i++){
            params[i] = args[i+1];
        }
        /* execute the operation */
        MBeanService service = ServiceFactory.getMBeanService();
        OperationResultData[] resultData =
                service.invoke(context.getServiceContext(),
                        expression.getAppName(),
                        expression.getMBeanName(),
                        expression.getTargetName(),
                        params);

        for(int i=0; i<resultData.length; i++){
            Out.println();
            Out.print(resultData[i].getApplicationName() + " > ");
            if(resultData[i].getResult() == OperationResultData.RESULT_ERROR){
                Out.println("Operation Failed.");
                Out.println("Error:");
                Out.println(resultData[i].getErrorString());
            }else{
                Out.println("Operation Successful. ");
                if(resultData[i].getOutput() != null){
                    Out.println("Result: ");
                    Out.println(resultData[i].getOutput());
                }else{
                    Out.println();
                }
            }
        }
        return true;
    }

    public String getShortHelp() {
        return "Executes given operation on mbean.";
    }

    public void help() {
        Out.println(getShortHelp());
        Out.println();
        Out.println("Usage:");
        Out.println(CommandConstants.EXECUTE +
                " <application name>/<mbean name>/<operation> [param1] [param2] ...");
        Out.println();
        Out.println("Examples:");
        Out.println(CommandConstants.EXECUTE +
                " myApp/myMBean/myOperation param");
    }

    private void usage(){
        Out.println("Invalid arguments");
        help();
    }
}
