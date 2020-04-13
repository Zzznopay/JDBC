package com.zzz.connection;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.activation.DataSource;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
/**
 * @description 测试C3P0的数据库连接池技术
 * @return
 * @throws Exception
 */
public class C3P0Test {
	//方式一:
	@Test
	public void testGetConnection() throws Exception{
		//获取c3p0数据库连接池
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
		cpds.setUser("root");
		cpds.setPassword("root");
		//通过设置相关的参数，对数据库的连接池进行管理
		//设置初始时数据库连接池中的的连接数
		cpds.setInitialPoolSize(10);
		Connection conn = cpds.getConnection();
		System.out.println(conn);
		//销毁c3p0数据库连接池
		//DataSources.destroy(cpds);
	}
	//方式二:使用配置文件
	@Test
	public void	testGetConnection1() throws Exception{
		ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
		Connection conn = cpds.getConnection();
		System.out.println(conn);
	}
	
}
