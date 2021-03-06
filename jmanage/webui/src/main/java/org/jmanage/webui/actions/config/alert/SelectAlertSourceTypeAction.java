/**
* Copyright (c) 2004-2005 jManage.org
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.jmanage.webui.actions.config.alert;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.AlertForm;
import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.config.ApplicationConfig;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>
 * Date:  Aug 3, 2005
 * @author	Rakesh Kalra
 */
public class SelectAlertSourceTypeAction extends BaseAction{

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {


        AlertForm alertForm = (AlertForm)actionForm;
        String sourceType = alertForm.getAlertSourceType();
        assert sourceType != null;
        ApplicationConfig appConfig= context.getApplicationConfig();
        
        if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_APPLICATION_DOWN)){
            String url = "/config/showAddAlert.do?"
                +  RequestParams.APPLICATION_ID + "="
                + appConfig.getApplicationId() + "&"
                + RequestParams.ALERT_SOURCE_TYPE + "="
                + AlertSourceConfig.SOURCE_TYPE_APPLICATION_DOWN;
            return new ActionForward(url);
        }else if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_GAUGE_MONITOR)){
            String url = "/config/showMBeans.do?"
                    +  RequestParams.APPLICATION_ID + "="
                    + appConfig.getApplicationId() + "&"
                    + RequestParams.END_URL + "="
                    + Utils.urlEncode("/config/showAddAlert.do")+ "&"
                    + RequestParams.MULTIPLE + "=false&"
                    + RequestParams.ALERT_SOURCE_TYPE + "="
                    + AlertSourceConfig.SOURCE_TYPE_GAUGE_MONITOR + "&"
                    + RequestParams.DATA_TYPE + "=java.lang.Number&"
                    //+ RequestParams.DATA_TYPE + "=javax.management.openmbean.CompositeData&"
                    + RequestParams.NAVIGATION + "=Add Alert";
            return new ActionForward(url);
        } else if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_STRING_MONITOR)){
            String url = "/config/showMBeans.do?"
                    +  RequestParams.APPLICATION_ID + "="
                    + appConfig.getApplicationId() + "&"
                    + RequestParams.END_URL + "="
                    + Utils.urlEncode("/config/showAddAlert.do")+ "&"
                    + RequestParams.MULTIPLE + "=false&"
                    + RequestParams.ALERT_SOURCE_TYPE + "="
                    + AlertSourceConfig.SOURCE_TYPE_STRING_MONITOR +"&"
                    + RequestParams.DATA_TYPE + "=java.lang.String&"
                    //+ RequestParams.DATA_TYPE + "=javax.management.openmbean.CompositeData&"
                    + RequestParams.NAVIGATION + "=Add Alert";;
            return new ActionForward(url);
        }
        return mapping.findForward(sourceType);
    }
}
