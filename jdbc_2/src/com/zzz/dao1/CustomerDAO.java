package com.zzz.dao1;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.zzz.bean.Customer;

/*
 * 此接口用于规范针对于customers表的常用操作
 */
public interface CustomerDAO {
	/**
	 * 
	 * @description 将cust对象添加到数据库中
	 * @author zzz   
	 * @date 2020年4月12日   
	 * @param conn
	 * @param cust
	 */
	void insert(Connection conn,Customer cust);
	/**
	 * 
	 * @description 针对于指定id，删除表中的一条记录
	 * @author zzz   
	 * @date 2020年4月12日 
	 * @param conn
	 * @param id
	 */
	void deleteById(Connection conn,int id);
	/**
	 * 
	 * @description 针对于内存中的cust对象，去修改数据表中指定的记录
	 * @author zzz   
	 * @date 2020年4月12日 
	 * @param conn
	 * @param cust
	 */
	void update(Connection conn,Customer cust);
	/**
	 * 
	 * @description 针对于指定的id查询得到对应的Customer对象
	 * @author zzz   
	 * @date 2020年4月12日 
	 * @param conn
	 * @param id
	 * @return
	 */
	Customer getCustomerById(Connection conn,int id);
	/**
	 * 
	 * @description 查询表中的所有记录构成的集合
	 * @author zzz   
	 * @date 2020年4月12日 
	 * @param conn
	 * @return
	 */
	List<Customer> getAll(Connection conn);
	/**
	 * 
	 * @description 返回数据表中的数据的条目数
	 * @author zzz   
	 * @date 2020年4月12日 
	 * @param conn
	 * @return
	 */
	Long getCount(Connection conn);
	/**
	 * 
	 * @description 返回数据表中最大的生日
	 * @author zzz   
	 * @date 2020年4月12日 
	 * @param conn
	 * @return
	 */
	Date getMaxBirth(Connection conn);
}
