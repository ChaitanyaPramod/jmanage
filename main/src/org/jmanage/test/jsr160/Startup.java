package org.jmanage.test.jsr160;

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public class Startup {

    public static void main(String[] args){
        JMXHelper.registerMBeans();
        while(true){
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
            }
        }
    }
}
