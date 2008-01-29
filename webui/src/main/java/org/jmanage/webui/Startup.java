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
package org.jmanage.webui;

import org.jmanage.core.util.CoreUtils;
import org.mortbay.jetty.Server;
import java.io.File;

/**
 * The Web-UI startup class.
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class Startup {

    public static void main(String[] args) throws Exception{
        /* start the application */
        start();
    }

    private static void start() throws Exception {
    	try{
	    	String jManageRoot = System.getProperty("JMANAGE_ROOT");
	    	File configDir = new File(jManageRoot + File.separator + "config");
	    	File configSrcDir = new File(jManageRoot + File.separator + "web" + File.separator + "META-INF" + File.separator + "config");
	    	CoreUtils.copyConfig(configDir, configSrcDir);
	        Server server = new Server(jManageRoot + File.separator + "config" + File.separator + "jetty-config.xml");
	        server.start();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
