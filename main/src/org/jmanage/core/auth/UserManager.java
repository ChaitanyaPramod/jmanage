package org.jmanage.core.auth;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.util.*;

/**
 * Date : Jun 27, 2004 10:43:03 PM
 * @author Shashank
 */
public class UserManager implements AuthConstants{
    private static UserManager userManager =
            new UserManager(new File(USER_CONFIG_FILE_NAME));

    /*  Last modified time for user configurations file */
    private static long lastModified = -1;
    private static Map users = null;

    /**
     * Cache user information.
     *
     * @param userConfigFile
     */
    private UserManager(File userConfigFile){
        try{
            lastModified = userConfigFile.lastModified();
            Document userConfig = new SAXBuilder().build(userConfigFile);
            users = loadUsers(userConfig);
        }catch(JDOMException jdEx){
            System.out.println("Error reading user info "+USER_CONFIG_FILE_NAME);
            jdEx.printStackTrace();
        }

    }


    /**
     * Invalidate cached information about configured applciations if the
     * configuration file got updated after last read.
     *
     * @return
     */
    public static UserManager getInstance(){
        File userConfiguration = new File(USER_CONFIG_FILE_NAME);
        if(lastModified < userConfiguration.lastModified()){
            /*  Refresh the cache   */
            userManager = new UserManager(userConfiguration);
        }
        return userManager;
    }

    /**
     * Load all users from the configuration file.
     *
     * @param userConfig
     * @return
     */
    private Map loadUsers(Document userConfig){
        Map userData = new HashMap(1);
        List users =
                userConfig.getRootElement().getChildren();
        Iterator userIterator = users.iterator();

        while(userIterator.hasNext()){
            Element user = (Element)userIterator.next();
            List roles = user.getChildren(ROLE);
            Iterator roleIterator = roles.iterator();
            List roleNames = new ArrayList();
            while(roleIterator.hasNext()){
                Element role = (Element)roleIterator.next();
                roleNames.add(role.getTextTrim());
            }
            userData.put(user.getAttributeValue(NAME),
                    new User(user.getAttributeValue(NAME),
                            user.getAttributeValue(PASSWORD),
                            roleNames));
        }
        return userData;
    }

    /**
     * Return instance of User with specified username and passowrd if exists.
     *
     * @param username
     * @param password
     * @return
     */
    public User getUser(String username, String password){
        User user = null;
        if(users.containsKey(username)){
            user = (User)users.get(username);
            user = password.equals(user.getPassword()) ? user : null;
        }
        return user;
    }
}