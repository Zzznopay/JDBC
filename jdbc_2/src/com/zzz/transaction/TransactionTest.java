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
 * 1.什么叫数据库事务？
 * 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。
 * 		> 一组逻辑操作单元：一个或多个DML操作。
 * 
 * 2.事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 * 当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存
 * 下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 * 
 * 3.数据一旦提交，就不可回滚
 * 
 * 4.哪些操作会导致数据的自动提交？
 * 		>DDL操作一旦执行，都会自动提交。
 * 			>set autocommit = false 对DDL操作失效
 * 		>DML默认情况下，一旦执行，就会自动提交。
 * 			>我们可以通过set autocommit = false的方式取消DML操作的自动提交。
 * 		>默认在关闭连接时，会自动的提交数据
 */
public class TransactionTest {
	//********************************未考虑数据库事务情况下的转帐操作********************************************
	 /* 
	 * 针对于数据表user_table来说:
	 * AA用户给BB用户转账100
	 * 
	 * update user_table set balance = balance - 100 where user = "AA";
	 * update user_table set balance = balance + 100 where user = "BB";
	 */
	@Test
	public void testUpdate(){
		String sql = "update user_table set balance = balance - 100 where user = ?";
		update(sql, "AA");
		//模拟网路异常
		System.out.println(10/0);//该异常导致AA钱扣了，但是BB没收到
		String sql1 = "update user_table set balance = balance + 100 where user = ?";
		update(sql1, "BB");
		System.out.println("转账成功");
	}
	//通用的增删改操作-------version 1.0
	public int update(String sql,Object ...args) {//sql占位符的个数与可变形参的长度相同
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			//填充占位符
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//小心参数声明
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);	
		}
		return 0;
	}
	//********************************考虑数据库事务情况下的转帐操作**********************************************	
	@Test
	public void testUpdateWithTx() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();//默认true
			
			//1.取消数据的自动提交
			conn.setAutoCommit(false);
			
			String sql = "update user_table set balance = balance - 100 where user = ?";
			update(conn,sql, "AA");
			
			//模拟网路异常
			System.out.println(10/0);//解决异常导致AA钱扣了，但是BB没收到
			String sql1 = "update user_table set balance = balance + 100 where user = ?";
			update(conn,sql1, "BB");
			System.out.println("转账成功");
			//2.提交数据
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			//3.回滚数据
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally{
			JDBCUtils.closeResources(conn, null);
		}		
	}
	//通用的增删改操作-------version 2.0(考虑上事务)
	public int update(Connection conn,String sql,Object ...args) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			//填充占位符
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//小心参数声明
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//修改其为自动提交数据，主要针对于使用数据库连接池时使用
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
		//获取当前连接的隔离级别
		System.out.println(conn.getTransactionIsolation());
		//设置数据库的隔离级别
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		//1.取消数据的自动提交
		conn.setAutoCommit(false);
		String sql = "select user,password,balance from user_table where user = ?";
		User user = getInstance(conn, User.class, sql, "CC");
		System.out.println(user);
	}
	@Test
	public void testTransactionUpdate() throws Exception{
		Connection conn = JDBCUtils.getConnection();

		//1.取消数据的自动提交
		conn.setAutoCommit(false);
		String sql = "update user_table set balance = ? where user = ?";
		update(conn, sql, 5000,"CC");
		
		Thread.sleep(15000);
		System.out.println("修改结束！");
	}
	//通用的查询操作，用于返回数据表中的一条记录(version 2.0:考虑事务)
	public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//小心参数声明
			}
			rs = ps.executeQuery();
			//获取结果集的元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			//通过ResultSetMetaData获取结果集的列数
			int columnCount = rsmd.getColumnCount();
			if(rs.next()){
				T t = clazz.newInstance();
				//处理结果集一行数据中的每一列
				for(int i = 0;i < columnCount;i ++){
					//获取列值
					Object columnValue = rs.getObject(i + 1);
					//获取每个列的列名
//					String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					//给t对象指定columnName属性，赋值为columnValue:通过反射
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
