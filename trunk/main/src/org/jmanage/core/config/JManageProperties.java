package org.jmanage.core.config;

import java.util.Properties;
import java.io.*;

/**
 * Date: Dec 4, 2004 2:38:01 AM
 * @author Shashank Bellary 
 */
public class JManageProperties extends Properties{
    public static String maxLoginAttempts = "login.maxAttempts";
    /*  The only instance   */
    private static JManageProperties jManageProperties = new JManageProperties();

    /**
     * The only constructor, which is priivate.
     */
    private JManageProperties(){
      super();
      try{
        InputStream property = new FileInputStream(ConfigConstants.JMANAGE_PROPERTY_FILE);
        load(property);
      }catch(FileNotFoundException fnfEx){
        fnfEx.printStackTrace();
      }catch(IOException ioe){
        ioe.printStackTrace();
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }

    /**
     * Gets an instance of the JManageProperties class. This is the only way
     * that any class can get and access a JManageProperties object, since the
     * constructor is private
     **/

    private static int maxLoginAttempt;

    public void storeMaxLoginAttempts(int maxLoginAttempt){
                this.setProperty("login.maxAttempts",
                Integer.toString(maxLoginAttempt));
        try{
           FileOutputStream fileOutputStream =
                 new FileOutputStream(ConfigConstants.JMANAGE_PROPERTY_FILE);
            this.store(fileOutputStream, null);
        } catch( Exception e){
            throw new RuntimeException(e);
        }
    }


    public static JManageProperties getInstance() {
      return jManageProperties;
    }
}
