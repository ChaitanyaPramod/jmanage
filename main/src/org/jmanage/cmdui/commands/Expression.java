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
package org.jmanage.cmdui.commands;

import java.util.StringTokenizer;

/**
 *
 * date:  Feb 26, 2005
 * @author	Rakesh Kalra
 */
class Expression {

    private static final String DELIMITER = "/";

    private final String exprString;
    private String appName;
    private String mbeanName;
    /* this could be attribute or operation name */
    private String targetName;

    public Expression(String exprString){
        this(exprString, null);
    }

    public Expression(String exprString, Expression context){

        this.exprString = exprString;
        StringTokenizer tokenizer = new StringTokenizer(exprString, DELIMITER);
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
}
