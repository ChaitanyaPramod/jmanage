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
package org.jmanage.testapp.mbeans;

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
