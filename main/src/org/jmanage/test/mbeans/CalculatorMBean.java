package org.jmanage.test.mbeans;

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public interface CalculatorMBean {

    /* operation with arguments */
    public int add(int a, int b);
    public int substract(int a, int b);
}
