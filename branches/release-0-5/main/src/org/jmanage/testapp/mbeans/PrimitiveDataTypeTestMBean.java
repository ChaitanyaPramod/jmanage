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
 * Time: 1:30:12 PM
 * To change this template use Options | File Templates.
 */
public interface PrimitiveDataTypeTestMBean {

    public int getInt();
    public void setInt(int a);
    public short getShort();
    public void setShort(short s);
    public long getLong();
    public void setLong(long l);
    public float getFloat();
    public void setFloat(float f);
    public double getDouble();
    public void setDouble(double d);
    public char getChar();
    public void setChar(char c);
    public boolean getBoolean();
    public void setBoolean(boolean bo);
    public byte getByte();
    public void setByte(byte b);
}
