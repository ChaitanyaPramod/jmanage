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
import org.jmanage.cmdui.util.CommandUtils;
import org.jmanage.cmdui.util.Out;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;

import java.util.List;

/**
 *
 * date:  Feb 23, 2005
 * @author	Rakesh Kalra
 */
public class QueryMBeansHandler implements CommandHandler {

    /**
     * Lists mbeans for the given application based on the filter (optional)
     *
     * @param context
     * @return true if the command was handled properly; false otherwise
     */
    public boolean execute(HandlerContext context) {
        String[] args = context.getCommand().getArgs();
        if(args.length != 1 && args.length != 2){
            usage();
            return false;
        }

        String appName = args[0];
        String filter = null;
        if(args.length > 1){
            filter = args[1];
        }

        MBeanService mbeanService = ServiceFactory.getMBeanService();
        List mbeanDataList =
                mbeanService.getMBeans(context.getServiceContext(),
                        appName, filter);
        assert mbeanDataList != null;
        CommandUtils.printMBeans(mbeanDataList);
        return true;
    }

    public String getShortHelp() {
        return "Lists mbeans for the given application";
    }

    public void help() {
        Out.println(getShortHelp());
        Out.println("Usage:");
        Out.println(CommandConstants.QUERY_MBEANS + " <application name> [filter]");
    }

    private void usage(){
        Out.println("Invalid arguments");
        help();
    }
}
