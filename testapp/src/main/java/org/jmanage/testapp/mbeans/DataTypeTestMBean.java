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
public interface DataTypeTestMBean {

    public Integer getInteger();
    public void setInteger(Integer a);
    public Short getShort();
    public void setShort(Short s);
    public Long getLong();
    public void setLong(Long l);
    public Float getFloat();
    public void setFloat(Float f);
    public Double getDouble();
    public void setDouble(Double d);
    public Character getCharacter();
    public void setCharacter(Character c);
    public Boolean getBoolean();
    public void setBoolean(Boolean bo);
    public Byte getByte();
    public void setByte(Byte b);
    public Date getDate();
    public void setDate(Date dt);
    public String[] getStringArray();
    public void setStringArray(String[] strArray);
    public String[] stringArrayOperation();
    public int[] getIntArray();
    public void setIntArray(int[] intArray);
    public int[] intArrayOperation();
    public void setNullString(String nullString);
    public String getNullString();
    public Boolean getNullBoolean();
    public void setNullBoolean(Boolean nullBoolean);
    public String[] getNullStrArray() ;
    public void setNullStrArray(String[] nullStrArray);
}
