package org.jmanage.test.mbeans;

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public class Calculator implements CalculatorMBean {

    /* operation with arguments */
    public int add(int a, int b) {
        return a + b;
    }

    public int substract(int a, int b){
        return a - b;
    }

    public int foo(){
        return 0;
    }
}
