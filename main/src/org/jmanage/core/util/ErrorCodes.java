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

/**
 *
 * date:  Feb 13, 2005
 * @author	Rakesh Kalra
 */
public interface ErrorCodes {

    String WEB_UI_ERROR_KEY = "error.message";
    String UNKNOWN_ERROR = "unknown.error";

    String INVALID_CREDENTIALS = "invalid.login";
    String ACCOUNT_LOCKED = "account.locked";
    String INVALID_LOGIN_ATTEMPTS = "invalid.login.attempt.count";

    String INVALID_APPLICATION_NAME = "invalid.appName";
    String OPERATION_NOT_SUPPORTED_FOR_CLUSTER = "cluster.unsupported.operation";

    String INVALID_OLD_PASSWORD = "invalid.oldPassword";
    String PASSWORD_MISMATCH = "mismatch.password";
    String CONNECTION_FAILED = "app.connection.failed";
}