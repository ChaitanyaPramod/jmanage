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
package org.jmanage.webui;

import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.PasswordField;
import org.jmanage.core.util.JManageProperties;

import java.net.Socket;
import java.net.InetAddress;
import java.io.OutputStream;

/**
 * Shutdown is used to remotely shut down the jManage Server. It uses
 * admin password to generate a key which is recognized by the remote
 * process to authorize a shutdown.
 *
 * @author Shashank Bellary
 * Date: Nov 29, 2005
 */
public class Shutdown {

	private static int _port = Integer.parseInt(JManageProperties.getStopPort());
	private static String _key;

	public static void main(String[] args) throws Exception {
		String jManageRoot = System.getProperty("JMANAGE_ROOT");
		CoreUtils.initJmanageForCLIUtilities(jManageRoot);
		UserManager userManager = UserManager.getInstance();
		User user = null;
		char[] password = null;
		int invalidAttempts = 0;

		if (args.length == 1) {
			password = args[0].toCharArray();
			user = userManager.verifyUsernamePassword(AuthConstants.USER_ADMIN, password);
			/* invalid password was tried */
			if (user == null) {
				invalidAttempts++;
			}
		}

		while (user == null) {
			if (invalidAttempts > 0) {
				System.out.println("Invalid Admin Password.");
			}
			/* get the password */
			password = PasswordField.getPassword("Enter password:");
			/* the password should match for the admin user */
			user = userManager.verifyUsernamePassword(AuthConstants.USER_ADMIN, password);
			invalidAttempts++;
			if (invalidAttempts >= 3) {
				break;
			}
		}

		/* exit if the admin password is still invalid */
		if (user == null) {
			System.out.println("Number of invalid attempts exceeded. Exiting !");
			return;
		}

		final JettyStopKey stopKey = new JettyStopKey(new String(password));
		_key = stopKey.toString();

		System.out.println("Shutting down...");

		stop();
	}

	/**
	 * Main shutdown method
	 */
	static void stop() {
		try {
			Socket s = new Socket(InetAddress.getLocalHost(), _port);
			OutputStream out = s.getOutputStream();
			out.write((_key + "\r\nstop\r\n").getBytes());
			out.flush();
			s.shutdownOutput();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
