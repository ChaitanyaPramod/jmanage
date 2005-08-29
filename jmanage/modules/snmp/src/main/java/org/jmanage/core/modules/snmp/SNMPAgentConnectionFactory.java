/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.jmanage.core.modules.snmp;

import org.jmanage.core.management.ServerConnectionFactory;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ConnectionFailedException;
import org.jmanage.core.config.ApplicationConfig;
import snmp.SNMPv1CommunicationInterface;

import java.net.InetAddress;

/**
 * @author shashank
 * Date: Jul 31, 2005
 */
public class SNMPAgentConnectionFactory implements ServerConnectionFactory{

    /**
     *
     * @param config
     * @return
     * @throws ConnectionFailedException
     */
    public ServerConnection getServerConnection(ApplicationConfig config)
            throws ConnectionFailedException {
        try{
            InetAddress hostAddress = InetAddress.getByName(config.getHost());
            SNMPv1CommunicationInterface commIntf =
                    new SNMPv1CommunicationInterface(1, hostAddress, "public");
            SNMPAgentConnection connection = new SNMPAgentConnection(commIntf);
            return connection;
        }catch(Throwable e){
            throw new ConnectionFailedException(e);
        }
    }
}
