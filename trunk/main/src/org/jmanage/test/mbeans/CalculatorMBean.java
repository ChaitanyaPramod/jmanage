package org.jmanage.test.mbeans;

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public interface CalculatorMBean {

    /* operation with arguments */
    public int add(int a, int b);
    public float addFloat(float a, float b);
    public double addDouble(double a, double b);
    public Integer addInteger(Integer a, Integer b);
    public int substract(int a, int b);
    public int multiply(int a, int b);
    public int divide(int a, int b);

}
