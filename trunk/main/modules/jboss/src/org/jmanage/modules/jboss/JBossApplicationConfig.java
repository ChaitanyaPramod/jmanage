package org.jmanage.modules.jboss;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.util.Loggers;

import java.util.logging.Logger;

/**
 *
 * date:  Oct 30, 2004
 * @author	Prem
 */
public class JBossApplicationConfig extends ApplicationConfig {
    private static final Logger logger =
            Loggers.getLogger(JBossApplicationConfig.class);

    public void setURL(String url){
        /* ignore the url */
        logger.fine("Ignoring url=" + url);
    }

    public String getURL() {
        return "jnp://" + getHost() + ":" + 1099;
    }
}
