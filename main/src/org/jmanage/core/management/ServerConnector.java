package org.jmanage.core.management;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.modules.ModuleConfig;
import org.jmanage.core.modules.ModulesRegistry;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
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

    private static final Logger logger =
            Loggers.getLogger(ServerConnector.class);

    /* classloaders for every module id */
    private static Map classLoaders = new HashMap();

    public static ServerConnection
            getServerConnection(ApplicationConfig appConfig){

        ModuleConfig moduleConfig =
                    ModulesRegistry.getModule(appConfig.getType());
        assert moduleConfig != null: "Invalid type=" + appConfig.getType();
        final ClassLoader classLoader = getClassLoader(moduleConfig);
        assert classLoader != null;

        final ClassLoader contextClassLoader =
                Thread.currentThread().getContextClassLoader();
        /* temporarily change the thread context classloader */
        Thread.currentThread().setContextClassLoader(classLoader);

        try {
            final ServerConnectionFactory factory =
                    getServerConnectionFactory(moduleConfig, classLoader);
            ServerConnection connection =
                    factory.getServerConnection(appConfig);
            return new ServerConnectionProxy(connection, classLoader);
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    private static ServerConnectionFactory
            getServerConnectionFactory(ModuleConfig moduleConfig,
                                       ClassLoader classLoader) {

        try {
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
            logger.info("Creating new ClassLoader for module: " +
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

    private static class MyClassLoader extends URLClassLoader {

        public MyClassLoader(URL[] urls){
            super(urls);
        }

        protected Class findClass(String name) throws ClassNotFoundException {
            try{
                Class clazz = super.findClass(name);
                logger.fine("loaded class: " + name);
                return clazz;
            }catch(ClassNotFoundException e){
                logger.fine("ClassNotFound: " + name);
                throw e;
            }
        }
    }
}
