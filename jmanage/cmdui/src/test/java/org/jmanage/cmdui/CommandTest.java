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
package org.jmanage.cmdui;

import junit.framework.TestCase;

/**
 *
 * date:  Feb 5, 2005
 * @author	Rakesh Kalra
 */
public class CommandTest extends TestCase {

    public CommandTest(String name){
        super(name);
    }

    public void testGet(){
        String[] args =
                new String[]{"-username", "admin", "-password", "123456"};
        try {
            Command command = Command.get(args);
            assertEquals(command.getUsername(), "admin");
            assertEquals(command.getPassword(), "123456");
        } catch (InvalidCommandException e) {
            fail(e.getMessage());
        }
    }

}
