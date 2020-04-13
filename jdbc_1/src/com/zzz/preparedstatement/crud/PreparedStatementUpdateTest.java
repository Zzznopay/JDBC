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
		//�޸�customers���һ������
		Connection conn = null;
		PreparedStatement ps =null;		
		try {
			//��ȡ����
			conn = JDBCUtils.getConnection();
			//Ԥ����sql��䣬����PreparedStatementʵ��
			String sql = "update customers set name = ? where id = ?";
			ps = conn.prepareStatement(sql);
			//���ռλ��
			ps.setObject(1, "zhangzezheng");
			ps.setObject(2, 19);
			//ִ�в���
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//�ر���Դ
			JDBCUtils.closeResources(conn, ps);
		}		
	}
	@Test
	public void testInsert() {
		//��customers�����һ����¼		
		Connection conn = null;
		PreparedStatement ps =null;
		try {
			//��ȡ�����ļ��е��ĸ�������Ϣ
			InputStream is = ConncectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties pros = new Properties();
			System.out.println(is);
			pros.load(is);
			is.close();					
			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");					
			//��������
			Class.forName(driverClass);	
			//��ȡ����		
			conn = DriverManager.getConnection(url,user,password);
			System.out.println(conn);
			//Ԥ����sql��䣬����PreparedStatementʵ��
			String sql = "insert into customers(name,email,birth)values(?,?,?)";//?��ռλ��
			ps = conn.prepareStatement(sql);
			//���ռλ��
			ps.setString(1, "zzz");
			ps.setString(2, "zzz@gmail.com");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("1997-05-23");				
			ps.setDate(3,new Date(date.getTime()));
			//ִ��sql
			ps.execute();
		} catch(Exception e) {			
			e.printStackTrace();
		} finally {
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
	}
	public void update(String sql,Object ...args) {//sqlռλ���ĸ�����ɱ��βεĳ�����ͬ
		//ͨ�õ���ɾ�Ĳ���
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//С�Ĳ�������
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
