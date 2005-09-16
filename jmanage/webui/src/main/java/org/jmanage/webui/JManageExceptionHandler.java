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

package org.jmanage.webui;

import org.apache.struts.action.*;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.Globals;
import org.jmanage.core.services.ServiceException;
import org.jmanage.core.util.ErrorCodes;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.ErrorCatalog;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.WebErrorCodes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Date: Feb 27, 2005 6:52:10 PM
 * @author Shashank Bellary 
 */
public class JManageExceptionHandler extends ExceptionHandler{

    private static final Logger logger =
            Loggers.getLogger(JManageExceptionHandler.class);

    /**
     * Custom handler of exceptions in jManage.
     *
     * @param exception
     * @param exConfig
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward execute(Exception exception, ExceptionConfig exConfig,
                                 ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws ServletException {

        ActionErrors errors = new ActionErrors();
        ActionForward forward;

        if(exception instanceof ServiceException){
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(WebErrorCodes.WEB_UI_ERROR_KEY,
                            exception.getMessage()));
        }else if(exception instanceof ConnectionFailedException){
            //TODO: We need not handle this condition once all the code throwing this exception gets moved to service layer.
            return mapping.findForward(Forwards.CONNECTION_FAILED);
        }else if(exception instanceof ApplicationConfigManager.DuplicateApplicationNameException){
            //TODO: We need not handle this exception once this exception
            // is handled in Service layer
            ApplicationConfigManager.DuplicateApplicationNameException ex  =
                    (ApplicationConfigManager.DuplicateApplicationNameException)exception;
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(WebErrorCodes.WEB_UI_ERROR_KEY,
                            ErrorCatalog.getMessage(ErrorCodes.APPLICATION_NAME_ALREADY_EXISTS,
                                    ex.getAppName())));
        }else{
            logger.log(Level.SEVERE, "unknown exception", exception);
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(ErrorCodes.UNKNOWN_ERROR));
        }
        request.setAttribute(Globals.ERROR_KEY, errors);
        forward = mapping.getInputForward();
        forward = forward != null && forward.getPath() != null ?
                forward : mapping.findForward(Forwards.FAILURE);
        forward = forward != null ? forward : new ActionForward(Forwards.FAILURE);
        return forward;
    }
}
