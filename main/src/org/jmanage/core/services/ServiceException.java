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
package org.jmanage.core.services;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class ServiceException extends RuntimeException {

    private String errorCode;
    private Object[] values;

    public ServiceException(String errorCode){
        this.errorCode = errorCode;
    }

    public ServiceException(String errorCode, Object value0){
        this(errorCode, new Object[]{value0});
    }

    public ServiceException(String errorCode, Object value0, Object value1){
        this(errorCode, new Object[]{value0, value1});
    }

    public ServiceException(String errorCode, Object value0, Object value1,
                            Object value2){
        this(errorCode, new Object[]{value0, value1, value2});
    }

    public ServiceException(String errorCode, Object[] values){
        this.errorCode = errorCode;
        this.values = values;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getValues() {
        return values;
    }

    public String getMessage(){
        // TODO: look in the message catalog and get an error string
        return "To be implemented:errorCode:" + errorCode;
    }
}
