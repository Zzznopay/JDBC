package com.zzz.utils1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCUtils1 {
	/**
	 * @description ʹ��C3P0�����ݿ����ӳؼ�����ȡ���ݿ�����
	 * @return
	 * @throws Exception
	 */
	//���ݿ����ӳ�ֻ���ṩһ������
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
	public static Connection getConnection1() throws Exception{
		Connection conn = cpds.getConnection();
		return conn;
	}
	/**
	 * @description ʹ��DBCP�����ݿ����ӳؼ�����ȡ���ݿ�����
	 * @return
	 * @throws Exception
	 */
	//����һ��DBCP���ݿ����ӳ�
	private static DataSource source;
	static {
		try {
			Properties pros = new Properties();
			//��ʽ2:
			FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
			pros.load(is);
			source = BasicDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection2() throws Exception{
		//��ʽ1:
		//InputStream is = ClassLoader.getSystemResourceAsStream("dbcp.properties");
		Connection conn = source.getConnection();
		return conn;
	}
	/**
	 * @description ʹ��Druid�����ݿ����ӳؼ�����ȡ���ݿ�����
	 * @return
	 * @throws Exception
	 */
	private static DataSource source1;
	static {
		try {
			Properties pros = new Properties();
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
			pros.load(is);
			source1 = DruidDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection3() throws Exception{	
		Connection conn = source1.getConnection();
		return conn;
	}
	public static void closeResource1(Connection conn,Statement ps,ResultSet rs){
//		try {
//			DbUtils.close(conn);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			DbUtils.close(ps);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			DbUtils.close(rs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		DbUtils.closeQuietly(conn);
		DbUtils.closeQuietly(ps);
		DbUtils.closeQuietly(rs);

	}
}
