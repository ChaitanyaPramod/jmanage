package org.jmanage.core.auth;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.input.SAXBuilder;
import org.jmanage.core.crypto.Crypto;

import java.io.File;
import java.io.FileOutputStream;
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
            /* no need to hash password, as it is stored in hash form in
                the database */
            userData.put(user.getAttributeValue(NAME),
                    new User(user.getAttributeValue(NAME),
                            user.getAttributeValue(PASSWORD),
                            roleNames, user.getAttributeValue(STATUS),
                            Integer.parseInt(user.getAttributeValue(LOCK_COUNT))));
        }
        return userData;
    }

    /**
     * Return instance of User with specified username and passowrd if exists.
     *
     * TODO: we should probably be more specific with the method name.
     * e.g.: verifyUsernamePassword
     *
     * @param username
     * @param password
     * @return
     */
    public User getUser(String username, char[] password){
        User user = (User)users.get(username);
        if(user != null){
            final String hashedPassword = Crypto.hash(password);
            user = hashedPassword.equals(user.getPassword()) && "A".equals(user.getStatus()) ? user : null;
        }
        return user;
    }

    /**
     * Overloaded for use in User management functionality.
     *
     * @param username
     * @return
     */
    public User getUser(String username){
        return users.containsKey(username) ? (User)users.get(username) : null;
    }

    /**
     * Retrieve all configured users of the application.
     *
     * @return
     */
    public Map getAllUsers(){
        return users;
    }

    /**
     * Add a new user to the list.
     *
     * @param user
     */
    public void addUser(User user){
        users.put(user.getName(), user);
        saveUser();
    }

    /**
     * Update the selected user information.
     *
     * @param user
     */
    public void updateUser(User user){
        users.remove(user.getName());
        users.put(user.getName(), user);
        saveUser();
    }

    /**
     * Remove the selected user from the list.
     *
     * @param username
     */
    public void deleteUser(String username){
        if(users.containsKey(username)){
            users.remove(username);
            saveUser();
        }
    }

    /**
     * Update current user's password.
     *
     * @param password
     */
    public void updatePassword(String username, String password){
        User user=(User)users.get(username);
        user.setPassword(Crypto.hash(password));
        saveUser();
    }

    /**
     * Save the changes to "jmanage-users.xml"
     */
    private void saveUser(){
        try {
            Document doc = new Document();
            Element rootElement = new Element(AuthConstants.JM_USERS);
            for(Iterator it=users.values().iterator(); it.hasNext();){
                User user = (User)it.next();
                /* create a user element */
                Element userElement = new Element(AuthConstants.USER);
                userElement.setAttribute(AuthConstants.NAME, user.getUsername());
                userElement.setAttribute(AuthConstants.PASSWORD, user.getPassword());
                userElement.setAttribute(AuthConstants.STATUS,
                        user.getStatus() != null ? user.getStatus() : "A");
                userElement.setAttribute(AuthConstants.LOCK_COUNT,
                        String.valueOf(user.getStatus() != null ? user.getLockCount() : 0));
                /* add roles */
                for(Iterator iterator = user.getRoles().iterator(); iterator.hasNext();){
                    String roleName = (String)iterator.next();
                    Element roleElement = new Element(AuthConstants.ROLE);
                    roleElement.setText(roleName);
                    userElement.addContent(roleElement);
                }
                rootElement.addContent(userElement);
            }
            doc.setRootElement(rootElement);
            /* write to the disc */
            XMLOutputter writer = new XMLOutputter();
            writer.output(doc, new FileOutputStream(AuthConstants.USER_CONFIG_FILE_NAME));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}