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

package org.jmanage.core.auth;

/**
 * Date : Apr 20, 2008 09:38:42 AM
 * @author Shashank
 */

public interface SSOToken {
	public static final String SSO_SUCCESS = "success";
	public static final String SSO_FAILURE = "failed";
	
	public String getSSOToken();
	public void setSSOError(String ssoError);
	public String getSSOError();
	public String getSSOStatus();

	public void setSSOStatus(String ssoStatus);

}
