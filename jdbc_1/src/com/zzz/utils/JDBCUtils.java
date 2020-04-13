package com.zzz.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/**
 * 
 * @description �������ݿ�Ĺ�����
 * @author zzz
 * @date 2020��4��7�� ����6:56:49
 * @version
 */
public class JDBCUtils {
	/**
	 * @description ��ȡ���ݿ������
	 * @author zzz
	 * @date 2020��4��7�� ����6:56:49
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception { 
		// ��ȡ�����ļ��е��ĸ�������Ϣ
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
//		System.out.println(is);
		pros.load(is);
		is.close();

		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");

		// ��������
		Class.forName(driverClass);

		// ��ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	/**
	 * @description �ر����Ӻ�statement�Ĳ���
	 * @author zzz
	 * @date 2020��4��7�� ����6:56:49
	 * @param conn
	 * @param ps
	 */
	public static void closeResources(Connection conn,Statement ps){
		//��Դ�ر�
		try {
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(ps != null){
				ps.close();
			}				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @description �ر���Դ
	 * @author zzz
	 * @date 2020��4��7�� ����6:56:49
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeResources(Connection conn,Statement ps,ResultSet rs){
		//��Դ�ر�
		try {
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(ps != null){
				ps.close();
			}				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(rs != null){
				rs.close();
			}				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
