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
import java.util.LinkedList;
import java.util.List;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.monitoring.data.model.ObservedMBeanAttribute;
import org.jmanage.util.db.DBUtils;

/**
 * @author rkalra
 *
 */
public class ObservedMBeanAttributeDAO extends DAO {

	public List<ObservedMBeanAttribute> find(ApplicationConfig appConfig){
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement("SELECT id, application_id, " +
            		"mbean_name, attribute_name, when_started, display_name " +
                    "FROM MBEAN_ATTRIBUTE" +
                    " WHERE application_id=? order by mbean_name,when_started desc");
            stmt.setString(1, appConfig.getApplicationId());
            rset = stmt.executeQuery();
            return resultSetToModel(rset);
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(rset);
            DBUtils.close(stmt);
            DBUtils.close(conn);
        }
	}

	public boolean isPresent(ObservedMBeanAttribute searchMBeanAttrib){
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement("SELECT id, application_id, " +
            		"mbean_name, attribute_name, when_started, display_name " +
                    "FROM MBEAN_ATTRIBUTE" +
                    " WHERE application_id=? AND mbean_name=? AND attribute_name=? order by mbean_name,when_started desc");
            stmt.setString(1, searchMBeanAttrib.getApplicationConfig().getApplicationId());
            stmt.setString(2, searchMBeanAttrib.getMBeanName());
            stmt.setString(3, searchMBeanAttrib.getAttributeName());
            rset = stmt.executeQuery();
            return rset.next();
            
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(rset);
            DBUtils.close(stmt);
            DBUtils.close(conn);
        }
	}	

	public int remove(long attribID){
		Connection conn = null;
        PreparedStatement stmt = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement("DELETE FROM MBEAN_ATTRIBUTE WHERE id=?");
            stmt.setLong(1, attribID);
            int result = stmt.executeUpdate();
            assert result==1;
            conn.commit();
            return result;
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(stmt);
            DBUtils.close(conn);
        }
	}		
	
	
	/**
	 * @return
	 */
	public List<ObservedMBeanAttribute> findAll() {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement("SELECT id, application_id, " +
            		"mbean_name, attribute_name, when_started, display_name " +
                    "FROM MBEAN_ATTRIBUTE order by mbean_name,when_started desc");
            rset = stmt.executeQuery();
            return resultSetToModel(rset);
        }catch(SQLException e){    
            throw new RuntimeException(e);
        }finally{
            DBUtils.close(rset);
            DBUtils.close(stmt);
            DBUtils.close(conn);
        }
	}

	private List<ObservedMBeanAttribute> resultSetToModel(ResultSet rset) throws SQLException{
        List<ObservedMBeanAttribute> observedMBeanAttributes = 
        	new LinkedList<ObservedMBeanAttribute>();
		while(rset.next()){
			final String appId = rset.getString(2);
			ApplicationConfig appConfig = 
				ApplicationConfigManager.getApplicationConfig(appId);
			if(appConfig == null){
				throw new RuntimeException("No application config found for appId: " + appId);
			}
        	observedMBeanAttributes.add(
        			new ObservedMBeanAttribute(
        					rset.getLong(1), 
        					appConfig, 
        					rset.getString(3), 
        					rset.getString(4),
        					rset.getTimestamp(5),
        					rset.getString(6)));
        }
        return observedMBeanAttributes;
	}

	public void save(List<ObservedMBeanAttribute> attributes){
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try{
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement("INSERT INTO MBEAN_ATTRIBUTE(id, application_id, " +
                    "mbean_name, attribute_name, when_started, display_name ) VALUES (NEXT VALUE FOR MBEAN_ATTRIBUTE_ID,?, ?, ?,?,?)");
            Timestamp whenStarted = new Timestamp(System.currentTimeMillis());
            for(ObservedMBeanAttribute attribute: attributes){
            	stmt.setString(1, attribute.getApplicationConfig().getApplicationId());
                stmt.setString(2, attribute.getMBeanName());
                stmt.setString(3, attribute.getAttributeName());
            	stmt.setTimestamp(4, whenStarted);
            	stmt.setString(5, attribute.getDisplayName());
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
