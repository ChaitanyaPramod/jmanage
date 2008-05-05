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
package org.jmanage.webui.actions.auth;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.SSOService;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.Utils;
import org.jmanage.core.services.AuthService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.JManageProperties;
import org.jmanage.core.util.Loggers;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date : Jul 5, 2004 10:54:41 PM
 * @author Shashank
 */
public class LogoutAction extends BaseAction {

	private Logger logger = Loggers.getLogger(LogoutAction.class);
	/**
	 * Logout the user.
	 *
	 * @param context
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward execute(WebContext context, ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AuthService authService = ServiceFactory.getAuthService();
		authService.logout(Utils.getServiceContext(context), context.getUser());
		context.removeUser();
		request.getSession(true).invalidate();
		/*
		 * The SSO interface logout() method could use the configured SSO logout URL to 
		 * redirect the request after performing necessary logout activities.
		 * Or optionally perform necessary logout activities and then return a SSO URL that 
		 * jManage could then do a redirect to.
		 */
		if(JManageProperties.isSSOEnabled()){
			try{
				SSOService ssoService = (SSOService)Class.forName(
						JManageProperties.getSSOServiceImplClassname()).newInstance();
				ssoService.logout(request, response);
				return null;
			}catch(Throwable e){
				logger.log(Level.SEVERE, e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}else{
			return mapping.findForward(Forwards.SUCCESS);
		}
	}
}
