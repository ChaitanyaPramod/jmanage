package org.jmanage.core.auth;

import javax.security.auth.callback.*;
import java.io.IOException;

/**
 * Date : Jun 27, 2004 1:34:02 PM
 * @author Shashank
 */
public class LoginCallbackHandler implements CallbackHandler {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param callbacks
     * @throws IOException
     * @throws UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nc = (NameCallback)callbacks[i];
                nc.setName(username);
            }else if(callbacks[i] instanceof PasswordCallback){
                PasswordCallback pc = (PasswordCallback)callbacks[i];
                pc.setPassword(password.toCharArray());
            } else {
                throw(new UnsupportedCallbackException(callbacks[i],
                        "Callback handler not supported"));
            }
        }
    }
}