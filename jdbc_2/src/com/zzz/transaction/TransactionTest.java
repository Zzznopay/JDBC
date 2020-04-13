package com.zzz.transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.zzz.bean.User;
import com.zzz.utils.JDBCUtils;
/*
 * 1.ʲô�����ݿ�����
 * ����һ���߼�������Ԫ,ʹ���ݴ�һ��״̬�任����һ��״̬��
 * 		> һ���߼�������Ԫ��һ������DML������
 * 
 * 2.�������ԭ�򣺱�֤����������Ϊһ��������Ԫ��ִ�У���ʹ�����˹��ϣ������ܸı�����ִ�з�ʽ��
 * ����һ��������ִ�ж������ʱ��Ҫô���е����񶼱��ύ(commit)����ô��Щ�޸ľ����õر���
 * ������Ҫô���ݿ����ϵͳ�����������������޸ģ���������ع�(rollback)�����״̬��
 * 
 * 3.����һ���ύ���Ͳ��ɻع�
 * 
 * 4.��Щ�����ᵼ�����ݵ��Զ��ύ��
 * 		>DDL����һ��ִ�У������Զ��ύ��
 * 			>set autocommit = false ��DDL����ʧЧ
 * 		>DMLĬ������£�һ��ִ�У��ͻ��Զ��ύ��
 * 			>���ǿ���ͨ��set autocommit = false�ķ�ʽȡ��DML�������Զ��ύ��
 * 		>Ĭ���ڹر�����ʱ�����Զ����ύ����
 */
public class TransactionTest {
	//********************************δ�������ݿ���������µ�ת�ʲ���********************************************
	 /* 
	 * ��������ݱ�user_table��˵:
	 * AA�û���BB�û�ת��100
	 * 
	 * update user_table set balance = balance - 100 where user = "AA";
	 * update user_table set balance = balance + 100 where user = "BB";
	 */
	@Test
	public void testUpdate(){
		String sql = "update user_table set balance = balance - 100 where user = ?";
		update(sql, "AA");
		//ģ����·�쳣
		System.out.println(10/0);//���쳣����AAǮ���ˣ�����BBû�յ�
		String sql1 = "update user_table set balance = balance + 100 where user = ?";
		update(sql1, "BB");
		System.out.println("ת�˳ɹ�");
	}
	//ͨ�õ���ɾ�Ĳ���-------version 1.0
	public int update(String sql,Object ...args) {//sqlռλ���ĸ�����ɱ��βεĳ�����ͬ
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			//���ռλ��
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//С�Ĳ�������
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);	
		}
		return 0;
	}
	//********************************�������ݿ���������µ�ת�ʲ���**********************************************	
	@Test
	public void testUpdateWithTx() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();//Ĭ��true
			
			//1.ȡ�����ݵ��Զ��ύ
			conn.setAutoCommit(false);
			
			String sql = "update user_table set balance = balance - 100 where user = ?";
			update(conn,sql, "AA");
			
			//ģ����·�쳣
			System.out.println(10/0);//����쳣����AAǮ���ˣ�����BBû�յ�
			String sql1 = "update user_table set balance = balance + 100 where user = ?";
			update(conn,sql1, "BB");
			System.out.println("ת�˳ɹ�");
			//2.�ύ����
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			//3.�ع�����
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally{
			JDBCUtils.closeResources(conn, null);
		}		
	}
	//ͨ�õ���ɾ�Ĳ���-------version 2.0(����������)
	public int update(Connection conn,String sql,Object ...args) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			//���ռλ��
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//С�Ĳ�������
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//�޸���Ϊ�Զ��ύ���ݣ���Ҫ�����ʹ�����ݿ����ӳ�ʱʹ��
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCUtils.closeResources(null, ps);	
		}
		return 0;
	}
	//**********************************************************************************************************
	@Test
	public void testTransactionSelect() throws Exception{
		Connection conn = JDBCUtils.getConnection();
		//��ȡ��ǰ���ӵĸ��뼶��
		System.out.println(conn.getTransactionIsolation());
		//�������ݿ�ĸ��뼶��
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		//1.ȡ�����ݵ��Զ��ύ
		conn.setAutoCommit(false);
		String sql = "select user,password,balance from user_table where user = ?";
		User user = getInstance(conn, User.class, sql, "CC");
		System.out.println(user);
	}
	@Test
	public void testTransactionUpdate() throws Exception{
		Connection conn = JDBCUtils.getConnection();

		//1.ȡ�����ݵ��Զ��ύ
		conn.setAutoCommit(false);
		String sql = "update user_table set balance = ? where user = ?";
		update(conn, sql, 5000,"CC");
		
		Thread.sleep(15000);
		System.out.println("�޸Ľ�����");
	}
	//ͨ�õĲ�ѯ���������ڷ������ݱ��е�һ����¼(version 2.0:��������)
	public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
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
			JDBCUtils.closeResources(null, ps, rs);
		}		
		return null;
	}
}
