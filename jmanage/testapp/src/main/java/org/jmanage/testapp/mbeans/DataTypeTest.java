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

import java.util.Date;

/**
 *
 * date:  Dec 23, 2004
 * @author	Vandana Taneja
 * @author Rakesh Kalra
 */
public class DataTypeTest implements DataTypeTestMBean {

    private Integer i=new Integer(9);
    private Short s=new Short((short)9);
    private Long l=new Long(9999);
    private Float f=new Float(9.9f);
    private Double d=new Double(9999.999);
    private Character c=new Character('a');
    private Boolean bo=new Boolean(true);
    private Byte b=new Byte((byte)1);
    private Date dt=new Date();
    private String[] strArray = new String[]{"abc", "xyz", "efg"};
    private int[] intArray = new int[]{1,2,3,4};
    private String nullString = null;
    private Boolean nullBoolean = null;
    private String[] nullStrArray = null;
    
    public Integer getInteger(){
        return i;
    }

    public void setInteger(Integer i){
        this.i = i;
    }

    public Short getShort(){
        return s;
    }

    public void setShort(Short s){
        this.s = s;
    }

    public Long getLong(){
        return l;
    }

    public void setLong(Long l){
        this.l = l;
    }

    public Float getFloat(){
        return f;
    }

    public void setFloat(Float f){
        this.f = f;
    }

    public Double getDouble(){
        return d;
    }
    public void setDouble(Double d){
        this.d = d;
    }

    public Character getCharacter(){
        return c;
    }

    public void setCharacter(Character c){
        this.c = c;
    }

    public Boolean getBoolean(){
        return bo;
    }

    public void setBoolean(Boolean bo){
        this.bo = bo;
    }

    public Byte getByte(){
        return b;
    }

    public void setByte(Byte b){
        this.b = b;
    }

    public Date getDate(){
        return dt;
    }

    public void setDate(Date dt){
        this.dt = dt;
    }

    public String[] getStringArray(){
        return strArray;
    }

    public void setStringArray(String[] strArray){
        this.strArray = strArray;
    }

    public String[] stringArrayOperation(){
        return strArray;
    }

    public int[] getIntArray(){
        return intArray;
    }

    public void setIntArray(int[] intArray){
        this.intArray = intArray;
    }

    public int[] intArrayOperation(){
        return intArray;
    }

    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    public String getNullString() {
        return nullString;
    }

    public Boolean getNullBoolean() {
        return nullBoolean;
    }

    public void setNullBoolean(Boolean nullBoolean) {
        this.nullBoolean = nullBoolean;
    }

    public String[] getNullStrArray() {
        return nullStrArray;
    }

    public void setNullStrArray(String[] nullStrArray) {
        this.nullStrArray = nullStrArray;
    }
}
