/**
 * jManage Application Management Platform 
 * Copyright 2004-2008 jManage.org
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. 
 */

package org.jmanage.core.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.auth.SSOToken;
import org.jmanage.core.util.ErrorCodes;
import org.jmanage.core.util.JManageProperties;
import org.jmanage.core.util.Loggers;

/**
 * Date : Apr 20, 2008 09:38:42 AM
 * 
 * @author Shashank
 */

public class SSOServiceImpl implements SSOService {
	private Logger logger = Loggers.getLogger(SSOServiceImpl.class);

	public SSOToken login(ServiceContext context, SSOToken ssoToken,
			String requestedResource) throws ServiceException {
		if ((requestedResource.equals("/auth/showLogin") || 
				requestedResource.equals("/auth/login"))) {
			logger.log(Level.SEVERE, "AMI login is requested. User must login through SSO.");
			throw new ServiceException(ErrorCodes.SSO_LOGIN_ERROR);
		}

		if (context.getUser() != null) {
			String username = context.getUser().getUsername();
			String userId = "test";
			//GuidUserMap.getUserId(ssoToken.getSSOToken());
			if (!username.equals(userId)) {
				logger.log(Level.SEVERE, "GUID in request and userId in session do not match. "
						+ "User name in session :" + username + " and GUID:" + ssoToken.getSSOToken());
				ssoToken.setSSOStatus(ssoToken.SSO_FAILURE);
				ssoToken.setSSOError("logout");
				return ssoToken;
			}
		}

    if(context.getUser() == null){
      if(!requestedResource.equals("/auth/showLogin") && 
      		!requestedResource.equals("/auth/login")){
      	
        logger.fine("GUID obtained in request:" + ssoToken.getSSOToken());
        //String userId = GuidUserMap.getUserId(ssoToken.getSSOToken());
        String userId = "test";
        logger.fine("User Id found for GUID:" + ssoToken.getSSOToken() +
        		" and User Id:"+ userId );
        if(userId != null){
        	AuthService authService = ServiceFactory.getAuthService();
      		try {
      			authService.login(context, userId, JManageProperties.getJManageDefaultPassword());
      		} catch (ServiceException e) {
            logger.log(Level.SEVERE,"Login failed for User Id :"+userId +" and GUID:" + 
            		ssoToken.getSSOToken() );
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR);
      		}
        }else{
          logger.log(Level.SEVERE,"User Id not found for GUID:" + ssoToken.getSSOToken());
          throw new ServiceException(ErrorCodes.UNAUTHORIZED_AMI_ACCESS);
        }
      }
    }
		return ssoToken;
	}

}
