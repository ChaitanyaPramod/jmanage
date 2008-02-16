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
