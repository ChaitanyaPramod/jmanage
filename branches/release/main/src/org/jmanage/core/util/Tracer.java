package org.jmanage.core.util;

/**
 *
 * date:  Jul 23, 2004
 * @author	Rakesh Kalra
 */
public class Tracer {

    public static void message(Object obj, String message){
        System.out.println(message);
    }

    public static void exception(Object obj, Throwable t) {
        if(t != null){
            t.printStackTrace();
        }
    }
}
