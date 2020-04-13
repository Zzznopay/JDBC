package com.zzz.preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.zzz.bean.Order;
import com.zzz.utils.JDBCUtils;

/**   
*      
* @description 针对于Order表的通用查询操作
* @author zzz   
* @date 2020年4月8日 上午11:54:36   
* @version        
*/
public class OrderForQuery {
	
	 /**
	 * 针对于表的字段名与类的属性名不相同的情况：
	 * 1. 必须声明sql时，使用类的属性名来命名字段的别名
	 * 2. 使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName(),
	 *    获取列的别名。
	 *  说明：如果sql中没有给字段其别名，getColumnLabel()获取的就是列名
	 * 
	 * 
	 */	
	@Test
	public void testOrderForQuery() {
		
		String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
		Order order = orderForQuery(sql, 1);
		System.out.println(order);
	}
	
	public Order orderForQuery(String sql,Object ...args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i ++){
				ps.setObject(i + 1, args);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if(rs.next()){
				Order order = new Order();
				for(int i = 0; i < columnCount;i ++){
					Object columnValue = rs.getObject(i+1);
					//获取列的列名：getColumnName();不推荐使用
					//获取列的别名：getColumnLabel();解决表列名字段名和对象类的属性名不一致
//					String columnName = rsmd.getColumnName(i+1);
					String columnLabel = rsmd.getColumnLabel(i+1);
					Field field = Order.class.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(order, columnValue);
				}
				return order;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps, rs);
		}
		
		return null;
	}
	
	@Test
	public void testQuery1() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			rs = ps.executeQuery();
			if(rs.next()){
				int id = (int) rs.getObject(1);
				String name = (String) rs.getObject(2);
				Date date = (Date) rs.getObject(3);
				
				Order order = new Order(id,name,date);
				System.out.println(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps, rs);
		}
		
	}
}
