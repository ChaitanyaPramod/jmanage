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
