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
	 * @description 使用C3P0的数据库连接池技术获取数据库连接
	 * @return
	 * @throws Exception
	 */
	//数据库连接池只需提供一个即可
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
	public static Connection getConnection1() throws Exception{
		Connection conn = cpds.getConnection();
		return conn;
	}
	/**
	 * @description 使用DBCP的数据库连接池技术获取数据库连接
	 * @return
	 * @throws Exception
	 */
	//创建一个DBCP数据库连接池
	private static DataSource source;
	static {
		try {
			Properties pros = new Properties();
			//方式2:
			FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
			pros.load(is);
			source = BasicDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection2() throws Exception{
		//方式1:
		//InputStream is = ClassLoader.getSystemResourceAsStream("dbcp.properties");
		Connection conn = source.getConnection();
		return conn;
	}
	/**
	 * @description 使用Druid的数据库连接池技术获取数据库连接
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
