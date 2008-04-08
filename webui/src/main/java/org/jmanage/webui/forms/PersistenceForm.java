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
package org.jmanage.webui.forms;


/**
 * Date: Mar 09, 2008 6:10:37 PM
 * @author Avneet
 */
@SuppressWarnings("serial")
public class PersistenceForm extends BaseForm{

    private String[] attributes;
    private String[] displayNames;
  
    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }
     
    public String[] getDisplayNames() {
        return displayNames;
    }

    public void setDisplayNames(String[] displayNames) {
        this.displayNames = displayNames;
    }

}
