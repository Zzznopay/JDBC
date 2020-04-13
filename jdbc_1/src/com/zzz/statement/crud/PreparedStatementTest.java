package com.zzz.statement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import org.junit.Test;

import com.zzz.utils.JDBCUtils;

/**   
*      
* @description ��ʾʹ��PreparedStatement�滻Statement
* @author zzz   
* @date 2020��4��8�� ����4:20:55   
* @version  
* 
* ���˽��Statement��ƴ����sql����֮�⣬PreparedStatement������Щ���ã�
* 1.PreparedStatement����Blob�����ݣ���Statement������
* 2.PreparedStatement����ʵ�ָ���Ч����������
* 
*/
public class PreparedStatementTest {
	@Test
	public void testLogin() {
		Scanner scan = new Scanner(System.in);

		System.out.print("�û���:");
		String user= scan.nextLine();
		System.out.print("��    ��:");
		String password = scan.nextLine();

		// SELECT user,password FROM user_table WHERE USER = '1' or ' AND PASSWORD = '
		// ='1' or '1' = '1';
		String sql = "SELECT user,password FROM user_table WHERE user = ? AND password = ?";
		User returnUser = getInstance( User.class,sql,user,password);
		if (returnUser != null) {
			System.out.println("��¼�ɹ�!");
		} else {
			System.out.println("�û������������");
		}
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
