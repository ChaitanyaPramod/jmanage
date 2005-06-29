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

import java.util.StringTokenizer;

/**
 *
 * date:  Feb 26, 2005
 * @author	Rakesh Kalra
 */
public class Expression {

    private static final String DELIMITER = "/";

    private final String exprString;
    private String appName;
    private String mbeanName;
    /* this could be attribute or operation name */
    private String targetName;

    public Expression(String appName, String mbeanName, String targetName){
        this.appName = appName!=null?appName:"";
        this.mbeanName = mbeanName!=null?mbeanName:"";
        this.targetName = targetName!=null?targetName:"";
        StringBuffer buff = new StringBuffer(this.appName);
        buff.append(DELIMITER);
        buff.append("\"");
        buff.append(this.mbeanName);
        buff.append("\"");
        buff.append(DELIMITER);
        buff.append(targetName);
        this.exprString = buff.toString();
    }

    public Expression(String exprString){
        this(exprString, (Expression)null);
    }

    public Expression(String exprString, Expression context){

        this.exprString = exprString;
        StringTokenizer tokenizer = new CustomStringTokenizer(exprString);
        if(context != null){
            this.appName = context.getAppName();
            this.mbeanName = context.getMBeanName();
            this.targetName = context.getTargetName();
            switch(tokenizer.countTokens()){
                case 1:
                    if(targetName != null){
                        targetName = tokenizer.nextToken();
                    }else if(mbeanName != null){
                        mbeanName = tokenizer.nextToken();
                    }else{
                        appName = tokenizer.nextToken();
                    }
                    break;
                case 2:
                    if(targetName != null){
                        mbeanName = tokenizer.nextToken();
                        targetName = tokenizer.nextToken();
                    }else{
                        appName = tokenizer.nextToken();
                        mbeanName = tokenizer.nextToken();
                    }
                    break;
                case 3:
                    appName = tokenizer.nextToken();
                    mbeanName = tokenizer.nextToken();
                    targetName = tokenizer.nextToken();
                    break;
                default:
                    // TODO: handle gracefully
                    throw new RuntimeException("Invalid expression");
            }

        }else{
            if(tokenizer.hasMoreTokens())
                appName = tokenizer.nextToken();
            if(tokenizer.hasMoreTokens())
                mbeanName = tokenizer.nextToken();
            if(tokenizer.hasMoreTokens())
                targetName = tokenizer.nextToken();
        }

    }

    public String getAppName() {
        return appName;
    }

    public String getMBeanName() {
        return mbeanName;
    }

    public String getTargetName() {
        return targetName;
    }

    public String toString(){
        return exprString;
    }

    private class CustomStringTokenizer extends StringTokenizer{

        public CustomStringTokenizer(String expr){
            super(expr, DELIMITER);
        }

        /**
         * Handles "/" within the expression.
         */
        public String nextToken(){
            String token = super.nextToken();
            if(token.startsWith("\"")){
                if(token.endsWith("\"")){
                    // token ends with double quotes. just drop the double quotes
                    token = token.substring(1, token.length() -1);
                }else{
                    /* token starts with double quotes, but doesn't end with it.
                        Keep getting next token,
                        till we find ending double quotes */
                    StringBuffer buff = new StringBuffer(token.substring(1));

                    while(true){
                        String nextToken = super.nextToken();
                        buff.append(DELIMITER);
                        if(nextToken.endsWith("\"")){
                            buff.append(nextToken.substring(0, nextToken.length()-1));
                            break;
                        }else{
                            buff.append(nextToken);
                        }
                    }
                    token = buff.toString();
                }
            }
            return token;
        }

    }
}
