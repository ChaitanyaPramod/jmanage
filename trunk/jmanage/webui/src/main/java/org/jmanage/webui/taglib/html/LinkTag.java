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
package org.jmanage.webui.taglib.html;

import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.util.WebContext;
import org.jmanage.core.services.AccessController;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * date:  Jun 20, 2004
 * @author	Rakesh Kalra
 */
public class LinkTag extends org.apache.struts.taglib.html.LinkTag {

    private String acl;
    private boolean hasAccess = true;

    public String getAcl(){
        return acl;
    }

    public void setAcl(String acl){
        this.acl = acl;
    }

    public int doStartTag() throws JspException{
        if(acl!=null){
            WebContext context = WebContext.get(
                    (HttpServletRequest)pageContext.getRequest());
            if(!AccessController.canAccess(Utils.getServiceContext(context),acl)){
                hasAccess = false;
                return SKIP_BODY;
            }
        }
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        if(!hasAccess){
            hasAccess = true;
            return EVAL_PAGE;
        }
        return super.doEndTag();
    }
    protected String calculateURL() throws JspException {
        String url = super.calculateURL();
        url = appendQueryStringParams(url);
        return url;
    }

    private String appendQueryStringParams(String url) {
        if(url.toLowerCase().startsWith("javascript:")){
            return url;
        }
        ServletRequest request = pageContext.getRequest();
        String applicationId =
                request.getParameter(RequestParams.APPLICATION_ID);
        if (applicationId != null) {
            url = Utils.appendURLParam(url, RequestParams.APPLICATION_ID,
                    applicationId);
        }
        String objectName =
                request.getParameter(RequestParams.OBJECT_NAME);
        if (objectName != null) {
            url = Utils.appendURLParam(url, RequestParams.OBJECT_NAME,
                    objectName);
        }
        return url;
    }
}
