/**
 * Copyright 2004-2005 jManage.org. All rights reserved.
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
package org.jmanage.webui.taglib.jm;

import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.taglib.HtmlElement;
import org.jmanage.webui.applets.GraphAppletParameters;
import org.jmanage.core.services.AccessController;
import org.jmanage.core.config.GraphConfig;
import org.jmanage.core.config.ApplicationConfig;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * <p>
 * Date:  Feb 4, 2006
 * @author	Rakesh Kalra
 */
public class GraphTag extends BaseTag {

    private String id;
    private int width = 600;
    private int height = 500;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int doStartTag() throws JspException{

        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        WebContext context = WebContext.get(request);
        ApplicationConfig appConfig = context.getApplicationConfig();
        // graphs at cluster level are not yet supported
        assert !appConfig.isCluster();
        GraphConfig graphConfig = appConfig.findGraph(id);

        HtmlElement applet = new HtmlElement("applet");
        applet.addAttribute("code", "org/jmanage/webui/applets/GraphApplet.class");
        applet.addAttribute("archive", "/applets/applets.jar,/applets/jfreechart-0.9.20.jar,/applets/jcommon-0.9.5.jar");
        applet.addAttribute("width", width);
        applet.addAttribute("height", height);

        ParamElement param = null;

        param = new ParamElement(GraphAppletParameters.GRAPH_TITLE, graphConfig.getName());
        applet.addChildElement(param);

        param = new ParamElement(GraphAppletParameters.POLLING_INTERVAL, graphConfig.getPollingInterval());
        applet.addChildElement(param);

        StringBuffer remoteBaseURL = request.getRequestURL();
        int i = remoteBaseURL.indexOf(request.getRequestURI());
        remoteBaseURL.delete(i, remoteBaseURL.length());
        String remoteURL = remoteBaseURL +
                "/app/fetchAttributeValues.do;jsessionid=" +
                Utils.getCookieValue(request, "JSESSIONID");
        param = new ParamElement(GraphAppletParameters.REMOTE_URL, remoteURL);
        applet.addChildElement(param);

        param = new ParamElement(GraphAppletParameters.ATTRIBUTE_DISPLAY_NAMES,
                graphConfig.getAttributeDisplayNames());
        applet.addChildElement(param);

        param = new ParamElement(GraphAppletParameters.ATTRIBUTES,
                graphConfig.getAttributesAsString());
        applet.addChildElement(param);

        param = new ParamElement(GraphAppletParameters.Y_AXIS_LABEL,
                graphConfig.getYAxisLabel());
        applet.addChildElement(param);

        if(graphConfig.getScaleFactor() != null){
            param = new ParamElement(GraphAppletParameters.SCALE_FACTOR,
                    graphConfig.getScaleFactor());
            applet.addChildElement(param);
        }

        if(graphConfig.isScaleUp() != null){
            param = new ParamElement(GraphAppletParameters.SCALE_UP,
                    graphConfig.isScaleUp());
            applet.addChildElement(param);
        }

        final JspWriter writer = pageContext.getOut();

        try {
            writer.println(applet.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    private class ParamElement extends HtmlElement{
        public ParamElement(String name, Object value){
            super("param");
            super.addAttribute("name", name);
            super.addAttribute("value", value);
        }

        public ParamElement(String name, int value){
            this(name, Integer.toString(value));
        }

        public ParamElement(String name, long value){
            this(name, Long.toString(value));
        }
    }
}
