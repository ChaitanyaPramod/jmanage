package org.jmanage.core.util;

import java.util.Date;
import java.io.*;

/**
 * Date: Nov 7, 2004 6:31:22 PM
 * @author Shashank Bellary
 */
public class UserActivityLogger {
    private static final String USER_ACTIVITY_LOG_FILE_NAME =
            CoreUtils.getLogDir() + "/userActivityLog.txt";
    private static UserActivityLogger logger = new UserActivityLogger();
    private static String activities;

    private UserActivityLogger() {
        loadActivity();
    }

    /**
     * Read the contents of userActivityLog file and cache it for further
     * updation.
     */
    private void loadActivity(){
        StringBuffer logBuffer = new StringBuffer();
        try{
            BufferedReader reader =
                    new BufferedReader(new FileReader(
                            new File(USER_ACTIVITY_LOG_FILE_NAME)));
            while((activities = reader.readLine()) != null){
                logBuffer.append(activities);
            }
            activities = logBuffer.toString();
        }catch(IOException ioe){
            throw new RuntimeException(ioe.getMessage());
        }
    }


    public static UserActivityLogger getInstance(){
        return logger;
    }

    /**
     * Logs user activity by capturing user actions and writing the same to the
     * userActivityLog file.
     *
     * @param user
     * @param activity
     * @param logInfo
     */
    public void log(String user, String activity, String logInfo){
        StringBuffer logBuffer = new StringBuffer(activities);
        logBuffer.append("\n");
        logBuffer.append(user+" performed "+activity+" on "+
                new Date().toString()+": Details ["+logInfo+"]");
        try{
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(
                            new File(USER_ACTIVITY_LOG_FILE_NAME)));
            writer.write(logBuffer.toString());
            writer.flush();
            writer.close();
        }catch(IOException ioe){
            throw new RuntimeException(ioe.getMessage());
        }
        loadActivity();
    }
}