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

import org.jmanage.core.auth.LoginCallbackHandler;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.config.JManageProperties;
import org.jmanage.core.util.ErrorCodes;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class AuthServiceImpl implements AuthService {


    final JManageProperties jManageProperties = JManageProperties.getInstance();
    private int MAX_LOGIN_ATTEMPTS_ALLOWED =
            Integer.parseInt(jManageProperties.getProperty(JManageProperties.maxLoginAttempts));

    /**
     * @see AuthService login()
     */
    public void login(ServiceContext context,
                      String username,
                      String password) {

        LoginCallbackHandler callbackHandler = new LoginCallbackHandler();
        callbackHandler.setUsername(username);
        callbackHandler.setPassword(password);
        // TODO: we should set this in startup or in startup script
        System.setProperty(AuthConstants.AUTH_CONFIG_SYS_PROPERTY,
                AuthConstants.AUTH_CONFIG_FILE_NAME);
        User user = null;
        UserManager userManager = UserManager.getInstance();
        try{
            LoginContext loginContext =
                    new LoginContext(AuthConstants.AUTH_CONFIG_INDEX,
                            callbackHandler);
            loginContext.login();
        }catch(LoginException lex){
            user = userManager.getUser(username);
            String errorCode = ErrorCodes.UNKNOWN_ERROR;
            Object[] values = null;
            /* Conditionalize the error message */
            if(user == null){
                errorCode = ErrorCodes.INVALID_CREDENTIALS;
            }else if("I".equals(user.getStatus())){
                errorCode = ErrorCodes.ACCOUNT_LOCKED;
            }else if(user.getLockCount() < MAX_LOGIN_ATTEMPTS_ALLOWED){
                int thisAttempt = user.getLockCount()+1;
                user.setLockCount(thisAttempt);
                if(thisAttempt == MAX_LOGIN_ATTEMPTS_ALLOWED){
                    user.setStatus("I");
                    userManager.updateUser(user);
                    errorCode = ErrorCodes.ACCOUNT_LOCKED;
                }else{
                    userManager.updateUser(user);
                    errorCode = ErrorCodes.INVALID_LOGIN_ATTEMPTS;
                    values = new Object[]{
                        String.valueOf(MAX_LOGIN_ATTEMPTS_ALLOWED - thisAttempt)};
                }
            }
            throw new ServiceException(errorCode, values);
        }
    }
}
