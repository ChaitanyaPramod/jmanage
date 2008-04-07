/**
 * jManage Application Management Platform 
 * Copyright 2004-2008 jManage.org
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. 
 */
package org.jmanage.monitoring.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.jmanage.monitoring.data.model.ObservedMBeanAttributeValue;
import org.jmanage.util.db.DBUtils;

/**
 * @author rkalra
 *
 */
public class ObservedMBeanAttributeValueDAO extends DAO {
	
	public void save(List<ObservedMBeanAttributeValue> attributeValues){
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement("INSERT INTO MBEAN_ATTRIBUTE_VALUE(when_collected, " +
                    "mbean_attribute_id, value) VALUES (?, ?, ?)");
            Timestamp whenCollected = new Timestamp(System.currentTimeMillis());
            for(ObservedMBeanAttributeValue attributeValue: attributeValues){
            	stmt.setTimestamp(1, whenCollected);
                stmt.setLong(2, attributeValue.getObservedMBeanAttribute().getId());
                stmt.setString(3, attributeValue.getValue());
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            for(int result:results){
            	assert result == 1;
            }
            conn.commit();
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(rset);
            DBUtils.close(stmt);
            DBUtils.close(conn);
        }
	}
}
