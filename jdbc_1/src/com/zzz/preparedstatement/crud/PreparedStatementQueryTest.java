package com.zzz.preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


import com.zzz.bean.Customer;
import com.zzz.bean.Order;
import com.zzz.utils.JDBCUtils;

/**   
*      
* @description ʹ��PreparedStatementʵ������ڲ�ͬ��ͨ�ò�ѯ����
* @author zzz   
* @date 2020��4��8�� ����1:51:13   
* @version        
*/
public class PreparedStatementQueryTest {
	@Test
	public void testGetForList(){
		String sql = "select id,name,email,birth from customers where id < ?";
		List<Customer> list = getForList(Customer.class, sql, 18);
		list.forEach(System.out::println);
	}

	public <T> List<T> getForList(Class<T> clazz,String sql,Object ...args){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//С�Ĳ�������
			}
			rs = ps.executeQuery();
			//��ȡ�������Ԫ����
			ResultSetMetaData rsmd = rs.getMetaData();
			//ͨ��ResultSetMetaData��ȡ�����������
			int columnCount = rsmd.getColumnCount();
			//�������϶���
			ArrayList<T> list = new ArrayList<T>();
			while(rs.next()){
				T t = clazz.newInstance();
				//��������һ�������е�ÿһ��
				for(int i = 0;i < columnCount;i ++){
					//��ȡ��ֵ
					Object columnValue = rs.getObject(i + 1);
					//��ȡÿ���е�����
//					String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					//��t����ָ��columnName���ԣ���ֵΪcolumnValue:ͨ������
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t,columnValue);
				}
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps, rs);
		}		
		return null;
	}
	
	@Test
	public void testGetInstance(){
		String sql = "select id,name,email,birth from customers where id = ?";
		Customer customer = getInstance(Customer.class, sql, 10);
		System.out.println(customer);
		
		String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
		Order order = getInstance(Order.class, sql1, 1);
		System.out.println(order);
	}
	/**
	 * 
	 * @description ����ڲ�ͬ�ı��ͨ�õĲ�ѯ���������ر��е�һ����¼
	 * @author zzz   
	 * @date 2020��4��8�� ����1:51:13   
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> T getInstance(Class<T> clazz,String sql,Object ...args){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//С�Ĳ�������
			}
			rs = ps.executeQuery();
			//��ȡ�������Ԫ����
			ResultSetMetaData rsmd = rs.getMetaData();
			//ͨ��ResultSetMetaData��ȡ�����������
			int columnCount = rsmd.getColumnCount();
			if(rs.next()){
				T t = clazz.newInstance();
				//��������һ�������е�ÿһ��
				for(int i = 0;i < columnCount;i ++){
					//��ȡ��ֵ
					Object columnValue = rs.getObject(i + 1);
					//��ȡÿ���е�����
//					String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					//��t����ָ��columnName���ԣ���ֵΪcolumnValue:ͨ������
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t,columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps, rs);
		}		
		return null;
	}
}
