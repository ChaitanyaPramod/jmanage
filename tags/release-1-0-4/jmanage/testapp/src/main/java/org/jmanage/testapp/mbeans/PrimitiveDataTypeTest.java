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
        private boolean bo2=true;
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

        public boolean getBoolean2(){
            return bo2;
        }

        public void setBoolean2(boolean bo){
            this.bo2 = bo;
        }

        public byte getByte(){
            return b;
        }

        public void setByte(byte b){
            this.b = b;
        }

}
