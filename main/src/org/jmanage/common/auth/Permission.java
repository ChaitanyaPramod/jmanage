package org.jmanage.common.auth;

import java.util.Map;

/**
 *
 * date:  Jun 4, 2004
 * @author	Rakesh Kalra
 */
public class Permission {

    private String name;
    private Map attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getAttributes() {
        return attributes;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }
}
