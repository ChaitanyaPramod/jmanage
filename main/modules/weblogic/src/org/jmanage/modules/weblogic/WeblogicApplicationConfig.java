package org.jmanage.modules.weblogic;

import org.jmanage.core.util.Loggers;
import org.jmanage.core.config.ApplicationConfig;

import java.util.logging.Logger;

/**
 *
 * date:  Jun 11, 2004
 * @author	Rakesh Kalra
 */
public class WeblogicApplicationConfig extends ApplicationConfig {

    private static final Logger logger =
            Loggers.getLogger(WeblogicApplicationConfig.class);

    public void setURL(String url){
        /* ignore the url */
        logger.fine("Ignoring url=" + url);
    }

    public String getURL() {
        return "t3://" + getHost() + ":" + getPort();
    }
}
