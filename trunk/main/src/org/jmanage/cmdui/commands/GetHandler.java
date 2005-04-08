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

import org.jmanage.cmdui.CommandConstants;
import org.jmanage.cmdui.CommandHandler;
import org.jmanage.cmdui.HandlerContext;
import org.jmanage.cmdui.util.CommandUtils;
import org.jmanage.cmdui.util.Out;
import org.jmanage.core.data.AttributeListData;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.Expression;

/**
 * Gets attribute values for given mbean.
 *
 * Date:  Mar 13, 2005
 * @author	Rakesh Kalra
 */
public class GetHandler implements CommandHandler {

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
        /* get the values */
        MBeanService service = ServiceFactory.getMBeanService();

        String[] attributeNames = null;
        if(args.length > 1){
            attributeNames = new String[args.length - 1];
            for(int i=0; i< attributeNames.length; i++){
                attributeNames[i] = args[i+1];
            }
        }
        ServiceContext serviceContext =
                context.getServiceContext(expression.getAppName(),
                            expression.getMBeanName());
        AttributeListData[] attributeValues = null;
        if(attributeNames != null){
            attributeValues = service.getAttributes(serviceContext,
                    attributeNames, true);
        }else{
            attributeValues = service.getAttributes(serviceContext);
        }
        CommandUtils.printAttributeLists(attributeValues);
        return true;
    }

    public String getShortHelp() {
        return "Gets attribute values for given mbean.";
    }

    public void help() {
        Out.println(getShortHelp());
        Out.println("Usage:");
        Out.println(CommandConstants.GET +
                " <application name>/<mbean name> [attribute1] [attribute2] ...");
        Out.println();
        Out.println("Examples:");
        Out.println(CommandConstants.GET +
                " myApp/myMBean");
        Out.println(CommandConstants.GET +
                " myApp/jmanage:name=TestMBean testAttr1 testAttr2");
    }

    /* TODO: we should probably move this to base class -rk */
    private void usage(){
        Out.println("Invalid arguments");
        help();
    }
}
