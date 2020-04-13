package com.zzz.blob;
/*
 * 使用PreparedStatement实现批量数据的操作
 * 
 * update、delete本身就具有批量操作的效果
 * 此时的批量操作，主要指的是插入。使用PreparedStatement如何实现更高效的批量操作？
 * 题目：向goods表中插入20000条数据
 * CREATE TABLE goods(
		id INT PRIMARY KEY AUTO_INCREMENT,
		NAME VARCHAR(25)
   );
 * 方式一：使用Statement
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
	//方式二：使用PreparedStatement
	@Test
	public void testInsert1() {//花费的时间为:38009
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
			System.out.println("花费的时间为:" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);
		}
	}
	//方式三：
	/*
	 * 1.addBatch(),executeBatch(),clearBatch()
	 * 2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
	 * 		 ?rewriteBatchedStatements=true 写在配置文件的url后面
	 * 3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
	 */
	@Test
	public void testInsert2() {//花费的时间为:523
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCUtils.getConnection();
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i =1;i <= 20000;i ++){
				ps.setObject(1, "name_" + i);
				//1.“攒”sql
				ps.addBatch();
				if(i % 500 == 0){
					//2.执行batch
					ps.executeBatch();
					//3.清空batch
					ps.clearBatch();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("花费的时间为:" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);
		}
	}
	//方式四：
	@Test
	public void testInsert3() {//花费的时间为:464(数据更大可以看见明显差距)
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCUtils.getConnection();
			//设置不允许自动提交数据了
			conn.setAutoCommit(false);
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i =1;i <= 20000;i ++){
				ps.setObject(1, "name_" + i);
				//1.“攒”sql
				ps.addBatch();
				if(i % 500 == 0){
					//2.执行batch
					ps.executeBatch();
					//3.清空batch
					ps.clearBatch();
				}
			}
			//提交数据
			conn.commit();
			long end = System.currentTimeMillis();
			System.out.println("花费的时间为:" + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);
		}
	}
}
