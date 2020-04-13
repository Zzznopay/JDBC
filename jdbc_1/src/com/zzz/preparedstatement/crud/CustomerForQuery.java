package com.zzz.preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.zzz.bean.Customer;
import com.zzz.utils.JDBCUtils;

/** 
*      
* @description �����Customers��Ĳ�ѯ����
* @author zzz   
* @date 2020��4��7�� ����7:43:13   
* @version
 */
public class CustomerForQuery {
	/** 
	*      
	* @description �����Customers���ͨ�ò�ѯ����
	* @author zzz   
	* @date 2020��4��7�� ����7:43:13   
	* @version
	 * @return 
	* @throws Exception 
	*/
	public Customer queryForCustomers(String sql,Object ...args) {
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
				Customer cust = new Customer();
				//��������һ�������е�ÿһ��
				for(int i = 0;i < columnCount;i ++){
					//��ȡ��ֵ
					Object columnValue = rs.getObject(i + 1);
					//��ȡÿ���е�����
//					String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					//��cust����ָ��columnName���ԣ���ֵΪcolumnValue:ͨ������
					Field field = Customer.class.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(cust,columnValue);
				}
				return cust;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps, rs);
		}		
		return null;
	}
	@Test
	public void testqueryForCustomers(){
		String sql = "select id,name,birth,email from customers where id = ? ";
		Customer customer = queryForCustomers(sql, 13);
		System.out.println(customer);
		
		sql = "select name,birth,email from customers where name = ?";
		Customer customer1 = queryForCustomers(sql, "zhangzezheng");
		System.out.println(customer1);
	}
	@Test
	public void testQuery1() {
		Connection conn = null;
		PreparedStatement ps = null;
		//ִ�У������ؽ����
		ResultSet resultSet = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "select  id,name,email,birth from customers where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			resultSet = ps.executeQuery();
			//��������
			if(resultSet.next()){//�жϽ��������һ���Ƿ������ݣ���������ݷ���true����ָ�����ƣ��������false��ָ�벻��				
				//��ȡ��ǰ���ݵĸ����ֶ�ֵ
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				Date birth = resultSet.getDate(4);
				//��ʽһ��
//				System.out.println("id = " + id + ",name = " + name + ",email = " + email + ",birth = " + birth );
				//��ʽ����
//				Object[] data = new Object[]{id,name,email,birth};
				//��ʽ���������ݷ�װΪһ�������Ƽ���
				Customer customer = new Customer(id,name,email,birth);
				System.out.println(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//�ر���Դ
			JDBCUtils.closeResources(conn, ps, resultSet);
		}
		
	}
}
