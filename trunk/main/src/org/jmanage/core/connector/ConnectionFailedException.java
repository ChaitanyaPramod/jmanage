package org.jmanage.core.connector;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class ConnectionFailedException extends RuntimeException{

    public ConnectionFailedException(Throwable cause){
        super(cause);
    }
}
