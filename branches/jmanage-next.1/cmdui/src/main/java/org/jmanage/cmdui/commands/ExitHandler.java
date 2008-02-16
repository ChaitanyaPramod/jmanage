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
import org.jmanage.cmdui.util.Out;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class ExitHandler implements CommandHandler {

    public boolean execute(HandlerContext context) {
        System.exit(0);
        return true;
    }

    public String getShortHelp(){
        return "Exits from jManage command prompt mode";
    }

    public void help() {
        Out.println(getShortHelp());
    }
}
