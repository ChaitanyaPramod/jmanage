package org.jmanage.testapp.mbeans;

import java.util.Date;

/**
 *
 * date:  Dec 23, 2004
 * @author	Vandana Taneja
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

    public Integer getInteger(){
        return i;
    }

    public void setInteger(Integer ii){
        this.i = i;
    }

    public Short getShort(){
        return s;
    }

    public void setShort(Short ss){
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

}
