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

    public float addFloat(float a, float b) {
        return a + b;
    }

    public double addDouble(double a, double b){
        return a + b;
    }

    public Integer addInteger(Integer a, Integer b){
        return  new Integer(a.intValue() + b.intValue());
    }

    public int substract(int a, int b){
        return a - b;
    }

    public int multiply(int a, int b) {
        return a*b;
    }

    public int divide(int a, int b) {
        return a/b;
    }

    public int foo(){
        return 0;
    }
}
