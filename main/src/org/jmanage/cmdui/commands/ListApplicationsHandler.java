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
import org.jmanage.core.services.ConfigurationService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.data.ApplicationConfigData;

import java.util.List;
import java.util.Iterator;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class ListApplicationsHandler implements CommandHandler {

    public boolean execute(HandlerContext context) {

        ConfigurationService configService =
                ServiceFactory.getConfigurationService();
        List appConfigDataList = configService.getAllApplications(
                context.getServiceContext());
        Out.println("Total " + appConfigDataList.size() + " applications");
        for(Iterator it=appConfigDataList.iterator(); it.hasNext();){
            ApplicationConfigData configData = (ApplicationConfigData)it.next();
            Out.print(configData.getName() + " [" + configData.getType() + "]" + "\t" + configData.getURL() + "\t" + configData.getUsername());
            if(configData.isCluster()){
                Out.println("\t*cluster*");
            }else{
                Out.println();
            }
        }
        return true;
    }

    public String getShortHelp() {
        return "Lists all configured applications in jManage";
    }

    public void help() {
        Out.println(getShortHelp());
    }
}
