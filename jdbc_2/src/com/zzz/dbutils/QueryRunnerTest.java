package com.zzz.dbutils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.zzz.bean.Customer;
import com.zzz.utils.JDBCUtils;
import com.zzz.utils1.JDBCUtils1;

/*
 * commons-dbutils�� Apache��֯�ṩ��һ����Դ JDBC������⣬��װ����������ݿ����ɾ�Ĳ������
 */
public class QueryRunnerTest {
	//���Բ���
	@Test
	public void TestInsert() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils1.getConnection3();
			String sql = "insert into customers(name,email,birth)values(?,?,?)";
			int insertCount = runner.update(conn, sql, "������","caixukun@zzz.com","1997-08-08");
			System.out.println("�����" + insertCount + "����¼");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}
	//���Բ�ѯ
	/*
	 * BeanHandler:��ResultSetHandler�ӿڵ�ʵ���࣬���ڷ�װ���е�һ����¼��
	 */
	@Test
	public void testQuery1() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils1.getConnection3();
			String sql = "select id,name,email,birth from customers where id = ?";
			BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
			Customer customer = runner.query(conn, sql, handler, 23);
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}
	/*
	 * BeanListHandler:��ResultSetHandler�ӿڵ�ʵ���࣬���ڷ�װ���еĶ�����¼��
	 */
	@Test
	public void testQuery2() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils1.getConnection3();
			String sql = "select id,name,email,birth from customers where id < ?";
			BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
			List<Customer> list = runner.query(conn, sql, handler, 23);
			list.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}
	/*
	 * MapHandler:��ResultSetHandler�ӿڵ�ʵ���࣬��Ӧ���е�һ����¼��
	 * ���ֶμ���Ӧ�ֶε�ֵ��Ϊmap�е�key��value
	 */
	@Test
	public void testQuery3() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils1.getConnection3();
			String sql = "select id,name,email,birth from customers where id = ?";
			MapHandler handler = new MapHandler();
			Map<String, Object> map = runner.query(conn, sql, handler, 23);
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}
	/*
	 * MapListHandler:��ResultSetHandler�ӿڵ�ʵ���࣬��Ӧ���еĶ�����¼��
	 * ���ֶμ���Ӧ�ֶε�ֵ��Ϊmap�е�key��value������Щmap��ӵ�List��
	 */
	@Test
	public void testQuery4() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils1.getConnection3();
			String sql = "select id,name,email,birth from customers where id < ?";
			MapListHandler handler = new MapListHandler();
			List<Map<String, Object>> list = runner.query(conn, sql, handler, 23);
			list.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}
	/*
	 * ScalarHandler:���ڲ�ѯ����ֵ
	 */
	@Test
	public void testQuery5() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils1.getConnection3();
			String sql = "select count(*) from customers";
			ScalarHandler handler = new ScalarHandler();
			Long count = (Long) runner.query(conn, sql, handler);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}
	@Test
	public void testQuery6() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils1.getConnection3();
			String sql = "select max(birth) from customers";
			ScalarHandler handler = new ScalarHandler();
			Date maxBirth = (Date) runner.query(conn, sql, handler);
			System.out.println(maxBirth);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}	
	/*
	 * �Զ���ResultSetHandler��ʵ����
	 */
	@Test
	public void testQuery7() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtils1.getConnection3();
			String sql = "select id,name,email,birth from customers where id = ?";
			ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>(){

				@Override
				public Customer handle(ResultSet rs) throws SQLException {
					//return null;
					if(rs.next()){
						int id = rs.getInt("id");
						String name = rs.getString("name");
						String email = rs.getString("email");
						Date birth = rs.getDate("birth");
						Customer cust = new Customer(id,name,email,birth);
						return cust;
					}
					return null;
				}
				
			};
			Customer customer = runner.query(conn, sql, handler,23);
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}	
}
