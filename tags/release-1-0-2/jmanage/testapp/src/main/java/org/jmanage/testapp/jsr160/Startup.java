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
package org.jmanage.testapp.jsr160;

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public class Startup {

    private static final int DEFAULT_PORT = 9999;

    public static void main(String[] args){

        int port = DEFAULT_PORT;
        boolean jmxmpConnector = false;

        for(int index=0; index < args.length; index++){
            if(args[index].equals("-port")){
                port = Integer.parseInt(args[++index]);
            }else if(args[index].equals("-jmxmp")){
                jmxmpConnector = true;
            }else{
                assert false:"Unknown argument:" + args[index];
            }
        }

        JMXHelper.registerMBeans(port, jmxmpConnector);
        while(true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
            }
        }
    }
}
