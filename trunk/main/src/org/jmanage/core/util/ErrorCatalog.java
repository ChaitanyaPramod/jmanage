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

import java.util.Properties;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;

/**
 *
 * date:  Feb 15, 2005
 * @author	Rakesh Kalra
 */
public class ErrorCatalog {

    private static final Logger logger = Loggers.getLogger(ErrorCatalog.class);

    private static final Properties errorMap;

    static{
        try {
            errorMap = new Properties();
            errorMap.load(new FileInputStream(CoreUtils.getConfigDir() +
                    "/errors.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMessage(String errorCode){
        return getMessage(errorCode, null);
    }

    public static String getMessage(String errorCode, Object[] values){
        String message = errorMap.getProperty(errorCode,
                "ErrorCode=" + errorCode);
        if(values != null){
            message = MessageFormat.format(message, values);
        }
        return message;
    }
}
