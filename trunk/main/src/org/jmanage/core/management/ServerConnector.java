package org.jmanage.core.management;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.modules.ModuleConfig;
import org.jmanage.core.modules.ModulesRegistry;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Tracer;

import java.util.Map;
import java.util.HashMap;
import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class ServerConnector {

    /* classloaders for every module id */
    private static Map classLoaders = new HashMap();

    public static ServerConnection
            getServerConnection(ApplicationConfig appConfig){

        ServerConnectionFactory factory = getServerConnectionFactory(appConfig);
        return factory.getServerConnection(appConfig);
    }

    private static ServerConnectionFactory
            getServerConnectionFactory(ApplicationConfig appConfig) {

        try {
            ModuleConfig moduleConfig =
                    ModulesRegistry.getModule(appConfig.getType());
            assert moduleConfig != null: "Invalid type=" + appConfig.getType();
            final ClassLoader classLoader = getClassLoader(moduleConfig);
            assert classLoader != null;
            final Class factoryClass =
                    Class.forName(moduleConfig.getConnectionFactory(),
                            true, classLoader);
            return (ServerConnectionFactory)factoryClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ClassLoader getClassLoader(ModuleConfig moduleConfig){
        ClassLoader classLoader =
                (ClassLoader)classLoaders.get(moduleConfig.getId());
        if(classLoader == null){
            Tracer.message(ServerConnector.class,
                    "Creating new ClassLoader for module: " +
                    moduleConfig.getId());
            URL[] classpath = getModuleClassPath(moduleConfig);
            classLoader = new URLClassLoader(classpath);
            classLoaders.put(moduleConfig.getId(), classLoader);
        }
        return classLoader;
    }

    private static URL[] getModuleClassPath(ModuleConfig moduleConfig){
        try {
            final String moduleDirPath =
                    CoreUtils.getModuleDir(moduleConfig.getId());
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
