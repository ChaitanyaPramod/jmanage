package org.jmanage.test.mbeans;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 *
 * date:  Dec 23, 2004
 * @author	Vandana Taneja
 */
public class BigDataTypeTest implements BigDataTypeTestMBean{

    private BigInteger bi = new BigInteger("9999");
    private BigDecimal bd = new BigDecimal("99999.999");

    public BigInteger getBigInteger(){
        return bi;
    }

    public void setBigInteger(BigInteger bi){
        this.bi = bi;
    }

    public BigDecimal getBigDecimal(){
        return bd;
    }

    public void setBigDecimal(BigDecimal bd){
        this.bd = bd;
    }
}
