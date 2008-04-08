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
package org.jmanage.webui.actions.config;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.PersistenceForm;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.services.AccessController;
import org.jmanage.core.util.Expression;
import org.jmanage.monitoring.data.dao.ObservedMBeanAttributeDAO;
import org.jmanage.monitoring.data.model.ObservedMBeanAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Date: Mar 09, 2008 6:10:37 PM
 * @author Avneet
 */
public class AddPersistenceAction extends BaseAction{
    /**
     * @param context
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
      AccessController.checkAccess(Utils.getServiceContext(context),ACL_ADD_PERSISTENCE);    	
        ApplicationConfig appConfig = context.getApplicationConfig();
        PersistenceForm form = (PersistenceForm)actionForm;
        String[] attributes = form.getAttributes();
        String[] displayNames = form.getDisplayNames();      
		ObservedMBeanAttributeDAO dao = new ObservedMBeanAttributeDAO();
		List<ObservedMBeanAttribute> failedMbeanAttrib = new ArrayList<ObservedMBeanAttribute>();
		
        ArrayList<ObservedMBeanAttribute> observedMBeanAttributes = new ArrayList<ObservedMBeanAttribute>();
        for(int i=0; i<attributes.length; i++){
            
        	//clear the save list, since saving in each iteration
        	observedMBeanAttributes.clear();
        	
        	//goto next element
        	Expression expression = new Expression(attributes[i]); 
            ObservedMBeanAttribute mAttrib = new ObservedMBeanAttribute(appConfig,expression.getMBeanName(),
            		expression.getTargetName(), new Date(),displayNames[i]);
            
            //chk for uniqueness
            boolean alreadyPresent = dao.isPresent(mAttrib);
            
            if(alreadyPresent)
            {
            	//configuration already present, add to failed list
            	failedMbeanAttrib.add(mAttrib);
            }
            else
            {
            	//add and save since not already present
                observedMBeanAttributes.add(mAttrib);
                dao.save(observedMBeanAttributes);  
            }
        }

//		if(failedMbeanAttrib.size() > 0) 
//	        return mapping.findForward(Forwards.FAILURE);
//		else
			return mapping.findForward(Forwards.SUCCESS);
    }
}
