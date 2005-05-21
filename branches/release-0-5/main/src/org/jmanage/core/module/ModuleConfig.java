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
package org.jmanage.core.module;

import org.jmanage.core.config.MetaApplicationConfig;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.io.File;
import java.util.logging.Logger;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ModuleConfig {

    private static final Logger logger = Loggers.getLogger(ModuleConfig.class);

    private String type;
    private String name;
    private MetaApplicationConfig metaConfig;
    private String connectionFactory;
    /* classloader for this module */
    private ClassLoader classLoader;

    public ModuleConfig(String type,
                        String name,
                        MetaApplicationConfig metaConfig,
                        String connectionFactory){
        this.type = type;
        this.name = name;
        this.metaConfig = metaConfig;
        this.connectionFactory = connectionFactory;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public MetaApplicationConfig getMetaApplicationConfig() {
        return metaConfig;
    }

    public String getConnectionFactory() {
        return connectionFactory;
    }

    public ClassLoader getClassLoader(){
        if(classLoader == null){
            logger.info("Creating new ClassLoader for module: " +
                    getType());
            URL[] classpath = getModuleClassPath();
            classLoader = new URLClassLoader(classpath);
        }
        return classLoader;
    }

    private URL[] getModuleClassPath(){
        try {
            final String moduleDirPath =
                    CoreUtils.getModuleDir(getType());
            final File moduleDir = new File(moduleDirPath);
            assert moduleDir.isDirectory():"Invalid moduleDir=" + moduleDirPath;
            File[] files = moduleDir.listFiles();
            URL[] urls = new URL[files.length];
            for(int i=0; i<files.length; i++){
                urls[i] = files[i].toURL();
            }
            return urls;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
