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
import org.jmanage.core.data.AttributeListData;
import org.jmanage.core.util.Expression;

import java.util.StringTokenizer;

/**
 * Sets one more attributes. The attribute values are specified in the following
 * manner:
 * <p>
 * attr1=value1 attr2=value2
 * <p><p>
 * This handler can't handle complex attribute values (e.g. with spaces). To set
 * complex attribute values, "set" command shoule be used.
 *
 * Date:  Mar 13, 2005
 * @author	Rakesh Kalra
 */
public class SetAttributesHandler implements CommandHandler {

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
        /* set the values */
        MBeanService service = ServiceFactory.getMBeanService();
        String[][] attributes = getAttributes(args);
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
     * attr1=value1 attr2=value2 ...
     *
     * @param args
     * @return
     */
    private String[][] getAttributes(String[] args) {
        String[][] attributes = new String[args.length-1][2];
        for(int i=0; i<args.length-1; i++){
            StringTokenizer tokenizer = new StringTokenizer(args[i+1], "=");
            assert tokenizer.countTokens() == 2;
            attributes[i][0] = tokenizer.nextToken();
            attributes[i][1] = tokenizer.nextToken();
        }
        return attributes;
    }

    public String getShortHelp() {
        return "Sets one or more attribute values.";
    }

    public void help() {
        Out.println(getShortHelp());
        Out.println("Usage:");
        Out.println(CommandConstants.SET_ATTRS +
                " <application name>/<mbean name> attribute1=value1 [attribute2=value2] ...");
        Out.println();
        Out.println("Examples:");
        Out.println(CommandConstants.SET_ATTRS +
                " myApp/myMBean testAtt1=value1");
        Out.println(CommandConstants.SET_ATTRS +
                " myApp/jmanage:name=TestMBean testAttr1=value1 testAttr2=value2");

    }

    private void usage(){
        Out.println("Invalid arguments");
        help();
    }
}
