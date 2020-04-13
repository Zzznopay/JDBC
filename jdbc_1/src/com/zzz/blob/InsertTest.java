package com.zzz.blob;
/*
 * ʹ��PreparedStatementʵ���������ݵĲ���
 * 
 * update��delete����;�������������Ч��
 * ��ʱ��������������Ҫָ���ǲ��롣ʹ��PreparedStatement���ʵ�ָ���Ч������������
 * ��Ŀ����goods���в���20000������
 * CREATE TABLE goods(
		id INT PRIMARY KEY AUTO_INCREMENT,
		NAME VARCHAR(25)
   );
 * ��ʽһ��ʹ��Statement
 * Connection conn = JDBCUtils.getConnection();
 * Statement st = conn.createStatement();
 * for(int i = 1;i <= 20000;i ++){
 * 		String sql = "insert into goods(name)values('name_" + i +"')";
 * 		st.execute(sql);	
 * 	}
 * 
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.junit.Test;

import com.zzz.utils.JDBCUtils;

public class InsertTest {
	//��ʽ����ʹ��PreparedStatement
	@Test
	public void testInsert1() {//���ѵ�ʱ��Ϊ:38009
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCUtils.getConnection();
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i =1;i <= 20000;i ++){
				ps.setObject(1, "name_" + i);
				ps.execute();
			}
			long end = System.currentTimeMillis();
			System.out.println("���ѵ�ʱ��Ϊ:" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);
		}
	}
	//��ʽ����
	/*
	 * 1.addBatch(),executeBatch(),clearBatch()
	 * 2.mysql������Ĭ���ǹر�������ģ�������Ҫͨ��һ����������mysql�����������֧�֡�
	 * 		 ?rewriteBatchedStatements=true д�������ļ���url����
	 * 3.ʹ�ø��µ�mysql ������mysql-connector-java-5.1.37-bin.jar
	 */
	@Test
	public void testInsert2() {//���ѵ�ʱ��Ϊ:523
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCUtils.getConnection();
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i =1;i <= 20000;i ++){
				ps.setObject(1, "name_" + i);
				//1.���ܡ�sql
				ps.addBatch();
				if(i % 500 == 0){
					//2.ִ��batch
					ps.executeBatch();
					//3.���batch
					ps.clearBatch();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("���ѵ�ʱ��Ϊ:" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);
		}
	}
	//��ʽ�ģ�
	@Test
	public void testInsert3() {//���ѵ�ʱ��Ϊ:464(���ݸ�����Կ������Բ��)
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCUtils.getConnection();
			//���ò������Զ��ύ������
			conn.setAutoCommit(false);
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i =1;i <= 20000;i ++){
				ps.setObject(1, "name_" + i);
				//1.���ܡ�sql
				ps.addBatch();
				if(i % 500 == 0){
					//2.ִ��batch
					ps.executeBatch();
					//3.���batch
					ps.clearBatch();
				}
			}
			//�ύ����
			conn.commit();
			long end = System.currentTimeMillis();
			System.out.println("���ѵ�ʱ��Ϊ:" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);
		}
	}
}
