package com.zzz.preparedstatement.crud;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Properties;

import org.junit.Test;

import com.zzz.connection.ConncectionTest;
import com.zzz.utils.JDBCUtils;

public class PreparedStatementUpdateTest {
	@Test
	public void testUpdate() {
		//修改customers表的一条数据
		Connection conn = null;
		PreparedStatement ps =null;		
		try {
			//获取连接
			conn = JDBCUtils.getConnection();
			//预编译sql语句，返回PreparedStatement实例
			String sql = "update customers set name = ? where id = ?";
			ps = conn.prepareStatement(sql);
			//填充占位符
			ps.setObject(1, "zhangzezheng");
			ps.setObject(2, 19);
			//执行操作
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭资源
			JDBCUtils.closeResources(conn, ps);
		}		
	}
	@Test
	public void testInsert() {
		//往customers表添加一条记录		
		Connection conn = null;
		PreparedStatement ps =null;
		try {
			//读取配置文件中的四个配置信息
			InputStream is = ConncectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties pros = new Properties();
			System.out.println(is);
			pros.load(is);
			is.close();					
			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");					
			//加载驱动
			Class.forName(driverClass);	
			//获取连接		
			conn = DriverManager.getConnection(url,user,password);
			System.out.println(conn);
			//预编译sql语句，返回PreparedStatement实例
			String sql = "insert into customers(name,email,birth)values(?,?,?)";//?：占位符
			ps = conn.prepareStatement(sql);
			//填充占位符
			ps.setString(1, "zzz");
			ps.setString(2, "zzz@gmail.com");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("1997-05-23");				
			ps.setDate(3,new Date(date.getTime()));
			//执行sql
			ps.execute();
		} catch(Exception e) {			
			e.printStackTrace();
		} finally {
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
	}
	public void update(String sql,Object ...args) {//sql占位符的个数与可变形参的长度相同
		//通用的增删改操作
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//小心参数声明
			}
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);	
		}
	}
	@Test
	public void testcommonUpdate(){
		String sql = "delete from customers where id = ?";
		update(sql, 12);
	}

}
