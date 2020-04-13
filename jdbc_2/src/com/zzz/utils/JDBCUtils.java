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
 * @description 操作数据库的工具类
 * @author zzz
 * @date 2020年4月7日 下午6:56:49
 * @version
 */
public class JDBCUtils {
	/**
	 * @description 获取数据库的连接
	 * @author zzz
	 * @date 2020年4月7日 下午6:56:49
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception { 
		// 读取配置文件中的四个配置信息
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
//		System.out.println(is);
		pros.load(is);
		is.close();

		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");

		// 加载驱动
		Class.forName(driverClass);

		// 获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	/**
	 * @description 关闭连接和statement的操作
	 * @author zzz
	 * @date 2020年4月7日 下午6:56:49
	 * @param conn
	 * @param ps
	 */
	public static void closeResources(Connection conn,Statement ps){
		//资源关闭
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
	 * @description 关闭资源
	 * @author zzz
	 * @date 2020年4月7日 下午6:56:49
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeResources(Connection conn,Statement ps,ResultSet rs){
		//资源关闭
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
