package com.zzz.preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.zzz.bean.Order;
import com.zzz.utils.JDBCUtils;

/**   
*      
* @description �����Order���ͨ�ò�ѯ����
* @author zzz   
* @date 2020��4��8�� ����11:54:36   
* @version        
*/
public class OrderForQuery {
	
	 /**
	 * ����ڱ���ֶ������������������ͬ�������
	 * 1. ��������sqlʱ��ʹ������������������ֶεı���
	 * 2. ʹ��ResultSetMetaDataʱ����Ҫʹ��getColumnLabel()���滻getColumnName(),
	 *    ��ȡ�еı�����
	 *  ˵�������sql��û�и��ֶ��������getColumnLabel()��ȡ�ľ�������
	 * 
	 * 
	 */	
	@Test
	public void testOrderForQuery() {
		
		String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
		Order order = orderForQuery(sql, 1);
		System.out.println(order);
	}
	
	public Order orderForQuery(String sql,Object ...args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i ++){
				ps.setObject(i + 1, args);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if(rs.next()){
				Order order = new Order();
				for(int i = 0; i < columnCount;i ++){
					Object columnValue = rs.getObject(i+1);
					//��ȡ�е�������getColumnName();���Ƽ�ʹ��
					//��ȡ�еı�����getColumnLabel();����������ֶ����Ͷ��������������һ��
//					String columnName = rsmd.getColumnName(i+1);
					String columnLabel = rsmd.getColumnLabel(i+1);
					Field field = Order.class.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(order, columnValue);
				}
				return order;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps, rs);
		}
		
		return null;
	}
	
	@Test
	public void testQuery1() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			rs = ps.executeQuery();
			if(rs.next()){
				int id = (int) rs.getObject(1);
				String name = (String) rs.getObject(2);
				Date date = (Date) rs.getObject(3);
				
				Order order = new Order(id,name,date);
				System.out.println(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps, rs);
		}
		
	}
}
