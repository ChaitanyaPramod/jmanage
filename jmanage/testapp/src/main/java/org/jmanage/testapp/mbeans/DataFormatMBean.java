/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.jmanage.testapp.mbeans;

/**
 *
 * <p>
 * Date:  Aug 31, 2005
 * @author	Rakesh Kalra
 */
public interface DataFormatMBean {

    /* XML */

    // test get and set
    public String getXML();
    public void setXML(String xml);
    // test read only attribute
    public String getXMLData();
    // test operation
    public String retrieveXMLData();

    /* HTML */

    // test get and set
    public String getHTML();
    public void setHTML(String html);
    // test read only attribute
    public String getHTMLData();
    // test operation
    public String retrieveHTMLData();
}
