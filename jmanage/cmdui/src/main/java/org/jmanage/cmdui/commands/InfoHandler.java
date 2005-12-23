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
import org.jmanage.cmdui.util.Table;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.management.ObjectOperationInfo;
import org.jmanage.core.management.ObjectAttributeInfo;
import org.jmanage.core.management.ObjectParameterInfo;
import org.jmanage.core.util.Expression;

/**
 * info       <appName>/<mbeanName[configured name or object name]>
 *
 * date:  Feb 23, 2005
 * @author	Rakesh Kalra
 */
public class InfoHandler implements CommandHandler {

    /**
     *
     * @param context
     * @return true if the command was handled properly; false otherwise
     */
    public boolean execute(HandlerContext context) {

        String[] args = context.getCommand().getArgs();
        if(args.length != 1){
            usage();
            return false;
        }

        Expression expression = new Expression(args[0]);
        if(expression.getAppName() == null || expression.getMBeanName() == null){
            usage();
            return false;
        }

        MBeanService service = ServiceFactory.getMBeanService();
        ObjectInfo objectInfo =
                service.getMBeanInfo(context.getServiceContext(
                        expression.getAppName(), expression.getMBeanName()));
        printObjectInfo(objectInfo);
        return true;
    }

    public String getShortHelp() {
        return "Display information about the mbean.";
    }

    public void help() {
        Out.println(getShortHelp());
        Out.println("Usage:");
        Out.println(CommandConstants.INFO + " <application name>/<mbean name>");
        Out.println("Examples:");
        Out.println(CommandConstants.INFO + " myApp/myMBean");
        Out.println(CommandConstants.INFO + " myApp/jmanage:name=TestMBean");
    }

    private void usage(){
        Out.println("Invalid arguments");
        help();
    }

    private void printObjectInfo(ObjectInfo objectInfo){
        Out.println();
        Out.println("Object Name: " + objectInfo.getObjectName());
        Out.println("Class Name : " + objectInfo.getClassName());
        Out.println("Description: " + objectInfo.getDescription());
        printAttributes(objectInfo.getAttributes());
        printOperations(objectInfo.getOperations());
    }

    private void printAttributes(ObjectAttributeInfo[] attributes) {
        if(attributes.length == 0)
            return;

        Out.println();
        Out.println("Attributes:");
        Table table = new Table(4);
        for(int i=0; i<attributes.length; i++){
            table.add(attributes[i].getName(),
                    attributes[i].getDisplayType(),
                    attributes[i].getReadWrite(),
                    attributes[i].getDescription());
        }
        table.print();
    }

    private void printOperations(ObjectOperationInfo[] operations) {
        if(operations.length == 0)
            return;

        Out.println();
        Out.println("Operations:");
        Table table = new Table(3);
        for(int i=0; i<operations.length; i++){
            table.add(operations[i].getName()+ "(" +
                    signature(operations[i].getSignature()) + ")",
                    operations[i].getDisplayReturnType(),
                    operations[i].getDescription());
        }
        table.print();
    }

    private String signature(ObjectParameterInfo[] signature){
        StringBuffer buff = new StringBuffer();
        for(int i=0; i<signature.length; i++){
            if(i > 0){
                buff.append(", ");
            }
            buff.append(signature[i].getDisplayType() + " " + signature[i].getName());
        }
        return buff.toString();
    }
}
