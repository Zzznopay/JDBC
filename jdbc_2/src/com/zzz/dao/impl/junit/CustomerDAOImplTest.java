package com.zzz.dao.impl.junit;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import org.junit.Test;

import com.zzz.bean.Customer;
import com.zzz.dao.impl.CustomerDAOImpl;
import com.zzz.utils.JDBCUtils;

public class CustomerDAOImplTest {
	private CustomerDAOImpl dao = new CustomerDAOImpl();
	@Test
	public void testInsert() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			Customer cust = new Customer(1 ,"admin", "admin@zzz.com", new Date(435346464435L));
			dao.insert(conn, cust);
			System.out.println("��ӳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}
	}

	@Test
	public void testDeleteById() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			dao.deleteById(conn, 22);
			System.out.println("ɾ���ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}	
	}

	@Test
	public void testUpdateConnectionCustomer() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			Customer cust = new Customer(20, "��С�","xiaolu@zzz.com",new Date(5645646546L));
			dao.update(conn, cust);
			System.out.println("�޸ĳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}	
	}

	@Test
	public void testGetCustomerById() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			Customer cust = dao.getCustomerById(conn, 19);
			System.out.println(cust);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}	
	}

	@Test
	public void testGetAll() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			List<Customer> list = dao.getAll(conn);
			list.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}	
	
	}

	@Test
	public void testGetCount() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			Long count = dao.getCount(conn);
			System.out.println("���еļ�¼��Ϊ:" + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}	
	
	}

	@Test
	public void testGetMaxBirth() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			Date maxBirth = dao.getMaxBirth(conn);
			System.out.println("��������:" + maxBirth);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, null);
		}	
	
	}

}
