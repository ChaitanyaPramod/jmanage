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
package org.jmanage.core.util;

import junit.framework.TestCase;

/**
 * Defines unit tests for Expression
 *
 * Date:  Jul 1, 2005
 * @author	Rakesh Kalra
 */
public class ExpressionTest extends TestCase {

    private static final String appName = "testApp";
    private static final String mbeanName = "jmanage:name=AppMBean";
    private static final String quotedMBeanName = "jmanage:name=\"AppMBean\"";
    private static final String attrName = "AppName";
    private static final String exprString = appName +
            Expression.DELIMITER + quotedMBeanName +
            Expression.DELIMITER + attrName;


    public ExpressionTest(String name){
        super(name);
    }

    public void testNormalCase(){
        Expression expression =
                new Expression(appName, mbeanName, attrName);
        assertEquals(expression.getAppName(), appName);
        assertEquals(expression.getMBeanName(), mbeanName);
        assertEquals(expression.getTargetName(), attrName);
    }

    public void testAllWildCard(){
        Expression expression = new Expression(null, null, null);
        assertEquals(expression.getAppName(), Expression.WILDCARD);
        assertEquals(expression.getMBeanName(), Expression.WILDCARD);
        assertEquals(expression.getTargetName(), Expression.WILDCARD);
    }

    public void testWildCardApplication(){
        Expression expression =
                new Expression("", mbeanName, attrName);
        assertEquals(expression.getAppName(), Expression.WILDCARD);
        assertEquals(expression.getMBeanName(), mbeanName);
        assertEquals(expression.getTargetName(), attrName);
    }

    public void testExprString(){
        Expression expression = new Expression(exprString);
        assertEquals(expression.getAppName(), appName);
        assertEquals(expression.getMBeanName(), quotedMBeanName);
        assertEquals(expression.getTargetName(), attrName);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Expression Context Tests

    public void testSimpleExpressionContext(){
        Expression context = new Expression(exprString);
        Expression expression = new Expression("attr2", context);
        assertEquals(expression.getAppName(), appName);
        assertEquals(expression.getMBeanName(), quotedMBeanName);
        assertEquals(expression.getTargetName(), "attr2");
    }

    public void testSimpleExpressionContext2(){
        final String mbeanWithDelimiter = "myNew/mbean";
        final String attr = "attr2";
        final String expr2 = "\"" + mbeanWithDelimiter + "\"" +
                Expression.DELIMITER + attr;
        Expression context = new Expression(exprString);
        Expression expression = new Expression(expr2, context);
        assertEquals(expression.getAppName(), appName);
        assertEquals(expression.getMBeanName(), mbeanWithDelimiter);
        assertEquals(expression.getTargetName(), attr);
    }

    public void testSimpleExpressionContext3(){
        final String appName2 = "testApp2";
        final String mbeanWithDelimiter = "myNew/mbean";
        final String attr = "attr2";
        final String expr2 = appName2 +
                Expression.DELIMITER + "\"" + mbeanWithDelimiter + "\"" +
                Expression.DELIMITER + attr;
        Expression context = new Expression(exprString);
        Expression expression = new Expression(expr2, context);
        assertEquals(expression.getAppName(), appName2);
        assertEquals(expression.getMBeanName(), mbeanWithDelimiter);
        assertEquals(expression.getTargetName(), attr);
    }
}
