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
import org.jmanage.cmdui.util.CommandUtils;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.data.AttributeListData;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Sets one attribute value at a time. This command can handle complex attribute
 * values.
 *
 * Date:  Mar 20, 2005
 * @author	Rakesh Kalra
 */
public class SetHandler implements CommandHandler {

    private static final Logger logger = Loggers.getLogger(SetHandler.class);

    /**
     *
     * @param context
     * @return true if the command was handled properly; false otherwise
     */
    public boolean execute(HandlerContext context) {

        String[] args = context.getCommand().getArgs();
        if(args.length < 3){
            usage();
            return false;
        }

        Expression expression = new Expression(args[0]);
        /* set the values */
        MBeanService service = ServiceFactory.getMBeanService();
        String[][] attributes = getAttributes(args);
        if(logger.isLoggable(Level.FINE)){
            logger.log(Level.FINE, "Setting attr. name=" + attributes[0][0] +
                    " value=" + attributes[0][1]);
        }
        AttributeListData[] attrListData =
                service.setAttributes(
                        context.getServiceContext(expression.getAppName(),
                                expression.getMBeanName()),
                        attributes);
        Out.println();
        Out.println("Changed Attributes:");
        CommandUtils.printAttributeLists(attrListData);
        return true;
    }

    /**
     * Starting from second argument, attribute and values are specified as
     * <p>
     * attr value
     *
     * @param args
     * @return
     */
    private String[][] getAttributes(String[] args) {
        String[][] attributes = new String[1][2];
        attributes[0][0] = args[1];
        /* The value may contain spaces, hence we need to concatenate remaining
            args */
        StringBuffer value = new StringBuffer();
        for(int i=2; i<args.length; i++){
            if(i>2)
                value.append(" ");
            value.append(args[i]);
        }
        attributes[0][1] = value.toString();
        return attributes;
    }

    public String getShortHelp() {
        return "Sets attribute value.";
    }

    public void help() {
        Out.println(getShortHelp() + " This command can handle complex " +
                "attribute values (e.g. with spaces)");
        Out.println("Usage:");
        Out.println(CommandConstants.SET +
                " <application name>/<mbean name> attribute value");
        Out.println();
        Out.println("Examples:");
        Out.println(CommandConstants.SET +
                " myApp/myMBean testAttr value");
        Out.println(CommandConstants.SET +
                " myApp/jmanage:name=TestMBean testAttr value");
    }

    private void usage(){
        Out.println("Invalid arguments");
        help();
    }
}

