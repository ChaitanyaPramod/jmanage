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
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }

        JMXHelper.registerMBeans(port);
        while(true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
            }
        }
    }
}
