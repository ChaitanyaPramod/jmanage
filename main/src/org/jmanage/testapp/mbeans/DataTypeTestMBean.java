package org.jmanage.testapp.mbeans;

import java.util.Date;

/**
 *
 * date:  Dec 23, 2004
 * @author	Vandana Taneja
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
}
