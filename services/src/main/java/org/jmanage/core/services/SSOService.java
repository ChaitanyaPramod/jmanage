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

import org.jmanage.core.auth.SSOToken;

/**
 * Date : Apr 20, 2008 09:38:42 AM
 * @author Shashank
 */

public interface SSOService {

  public SSOToken login(ServiceContext context, SSOToken ssoToken, 
  		String requestedResource) throws ServiceException;

}
