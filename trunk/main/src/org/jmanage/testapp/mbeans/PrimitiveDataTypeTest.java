package org.jmanage.testapp.mbeans;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Dec 23, 2004
 * Time: 1:33:56 PM
 * To change this template use Options | File Templates.
 */
public class PrimitiveDataTypeTest implements PrimitiveDataTypeTestMBean{

        private int i=9;
        private short s=9;
        private long l=9999;
        private float f=9.9f;
        private double d=9999.999;
        private char c='a';
        private boolean bo=true;
        private byte b=1;


        public int getInt(){
            return i;
        }

        public void setInt(int i){
            this.i = i;
        }

        public short getShort(){
            return s;
        }

        public void setShort(short s){
            this.s = s;
        }

        public long getLong(){
            return l;
        }

        public void setLong(long l){
            this.l = l;
        }

        public float getFloat(){
            return f;
        }

        public void setFloat(float f){
            this.f = f;
        }

        public double getDouble(){
            return d;
        }

        public void setDouble(double d){
            this.d = d;
        }

        public char getChar(){
            return c;
        }

        public void setChar(char c){
            this.c = c;
        }

        public boolean getBoolean(){
            return bo;
        }

        public void setBoolean(boolean bo){
            this.bo = bo;
        }

        public byte getByte(){
            return b;
        }

        public void setByte(byte b){
            this.b = b;
        }

}
