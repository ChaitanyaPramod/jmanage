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
import org.jmanage.cmdui.CommandConstants;
import org.jmanage.cmdui.HandlerContext;
import org.jmanage.cmdui.util.Out;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.management.ObjectAttribute;
import org.jmanage.core.data.AttributeListData;
import org.jmanage.util.StringUtils;

import java.util.*;

/**
 *
 * date:  Feb 26, 2005
 * @author	Rakesh Kalra
 */
public class PrintHandler implements CommandHandler {

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

        /* parse the expression and get the hashmap of appname+mbeanname to
            attribute list */
        final Map appMBeanToAttributesMap = new HashMap();

        Expression expression = null;
        for(int i=0; i<args.length; i++){
            expression = new Expression(args[i], expression);
            if(expression.getAppName() == null ||
                    expression.getMBeanName() == null ||
                    expression.getTargetName() == null){
                usage();
                return false;
            }
            AppMBeanKey key = new AppMBeanKey(expression.getAppName(),
                    expression.getMBeanName());
            List attributes = (List)appMBeanToAttributesMap.get(key);
            if(attributes == null){
                attributes = new LinkedList();
                appMBeanToAttributesMap.put(key, attributes);
            }
            attributes.add(expression.getTargetName());
        }

        /* now get the values */
        MBeanService service = ServiceFactory.getMBeanService();
        for(Iterator it=appMBeanToAttributesMap.keySet().iterator(); it.hasNext();){
            AppMBeanKey key = (AppMBeanKey)it.next();
            List attributes = (List)appMBeanToAttributesMap.get(key);
            AttributeListData[] attributeValues =
                    service.getAttributes(
                            context.getServiceContext(key.appName, key.mbeanName),
                            StringUtils.listToStringArray(attributes),
                            false);
            print(attributeValues);
        }
        Out.println();

        return true;
    }

    public String getShortHelp() {
        return "Prints tab delimited value(s) for given mbean attribute(s).";
    }

    public void help() {
        Out.println(getShortHelp());
        Out.println("Usage:");
        Out.println(CommandConstants.PRINT +
                " <application name>/<mbean name>/<attribute1> [attribute2] ...");
        Out.println();
        Out.println("Examples:");
        Out.println(CommandConstants.PRINT +
                " myApp/myMBean/testAttr1 testAttr2");
        Out.println(CommandConstants.PRINT +
                " myApp/jmanage:name=TestMBean/testAttr1 jmanage:name=Configuration/testAttr");
    }

    private void print(AttributeListData[] attrValues){
        assert attrValues.length == 1;
        for(Iterator it=attrValues[0].getAttributeList().iterator(); it.hasNext(); ){
            ObjectAttribute attribute= (ObjectAttribute)it.next();
            Out.print(attribute.getValue().toString() + "\t");
        }
    }

    private void usage(){
        Out.println("Invalid arguments");
        help();
    }

    private class AppMBeanKey{
        String appName;
        String mbeanName;

        AppMBeanKey(String appName, String mbeanName){
            this.appName = appName;
            this.mbeanName = mbeanName;
        }

        public int hashCode(){
            return (appName.hashCode() * 31) + mbeanName.hashCode();
        }

        public boolean equals(Object obj){
            if(obj instanceof AppMBeanKey){
                AppMBeanKey key = (AppMBeanKey)obj;
                return key.appName.equals(this.appName) &&
                        key.mbeanName.equals(this.mbeanName);
            }
            return false;
        }
    }
}
