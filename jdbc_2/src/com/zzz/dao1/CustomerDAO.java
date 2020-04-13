package com.zzz.dao1;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.zzz.bean.Customer;

/*
 * �˽ӿ����ڹ淶�����customers��ĳ��ò���
 */
public interface CustomerDAO {
	/**
	 * 
	 * @description ��cust������ӵ����ݿ���
	 * @author zzz   
	 * @date 2020��4��12��   
	 * @param conn
	 * @param cust
	 */
	void insert(Connection conn,Customer cust);
	/**
	 * 
	 * @description �����ָ��id��ɾ�����е�һ����¼
	 * @author zzz   
	 * @date 2020��4��12�� 
	 * @param conn
	 * @param id
	 */
	void deleteById(Connection conn,int id);
	/**
	 * 
	 * @description ������ڴ��е�cust����ȥ�޸����ݱ���ָ���ļ�¼
	 * @author zzz   
	 * @date 2020��4��12�� 
	 * @param conn
	 * @param cust
	 */
	void update(Connection conn,Customer cust);
	/**
	 * 
	 * @description �����ָ����id��ѯ�õ���Ӧ��Customer����
	 * @author zzz   
	 * @date 2020��4��12�� 
	 * @param conn
	 * @param id
	 * @return
	 */
	Customer getCustomerById(Connection conn,int id);
	/**
	 * 
	 * @description ��ѯ���е����м�¼���ɵļ���
	 * @author zzz   
	 * @date 2020��4��12�� 
	 * @param conn
	 * @return
	 */
	List<Customer> getAll(Connection conn);
	/**
	 * 
	 * @description �������ݱ��е����ݵ���Ŀ��
	 * @author zzz   
	 * @date 2020��4��12�� 
	 * @param conn
	 * @return
	 */
	Long getCount(Connection conn);
	/**
	 * 
	 * @description �������ݱ�����������
	 * @author zzz   
	 * @date 2020��4��12�� 
	 * @param conn
	 * @return
	 */
	Date getMaxBirth(Connection conn);
}
