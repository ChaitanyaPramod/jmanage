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

import org.mortbay.jetty.Server;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.io.LineNumberReader;
import java.io.InputStreamReader;

/**
 * @author Shashank Bellary
 * Date: Nov 29, 2005
 */
public class ServerMonitor extends Thread{
    private int _port = Integer.getInteger("STOP.PORT", 9999).intValue();
    private String _key = System.getProperty("STOP.KEY", "jManageKey");
    private ServerSocket _socket;
    private final String STOP_CMD = "stop";

    /**
     * Default Constructor, initializing basic shutdown parameters.
     */
    ServerMonitor(){
        try{
            if(_port < 0)
                return;
            setDaemon(true);
            _socket = new ServerSocket(_port, 1, InetAddress.getLocalHost());
            if(_port == 0){
                _port = _socket.getLocalPort();
                System.out.println(_port);
            }
            if("jManageKey".equals(_key)){
                System.out.println(_key);
            }
        }catch(Exception e){
            System.err.println(e.toString());
        }
        if(_socket != null)
            this.start();
        else
            System.out.println("WARN: Not listening on monitor port: "+_port);
    }

    /**
     * Deamon's main method.
     */
    public void run(){
        while(true){
            Socket socket = null;
            try{
                socket = _socket.accept();
                LineNumberReader lin =
                        new LineNumberReader(new InputStreamReader(socket.getInputStream()));
                String key = lin.readLine();
                if(!_key.equals(key))
                    continue;

                String cmd = lin.readLine();
                if(STOP_CMD.equals(cmd)){
                    try{
                        socket.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    try{
                        _socket.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }catch(Exception e){
                System.out.println(e.toString());
            }finally{
                if(socket != null){
                    try{
                        socket.close();
                    }catch(Exception e){}
                }
                socket = null;
            }
        }
    }

    /**
     * Start a Monitor.
     * This static method starts a monitor that listens for admin requests.
     */
    public static void monitor(){
        new ServerMonitor();
    }
}