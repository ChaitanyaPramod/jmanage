package org.jmanage.test.mbeans;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 *
 * date:  Dec 23, 2004
 * @author	Vandana Taneja
 */
public interface BigDataTypeTestMBean {

    public BigInteger getBigInteger();
    public void setBigInteger(BigInteger bi);
    public BigDecimal getBigDecimal();
    public void setBigDecimal(BigDecimal bd);

}
