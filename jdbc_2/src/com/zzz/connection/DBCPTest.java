package com.zzz.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;
/**
 * @description 测试DBCP的数据库连接池技术
 * @return
 * @throws Exception
 */
public class DBCPTest {	
	//方式一:不推荐
	@Test
	public void testGetConnection() throws Exception{
		//创建了DBCP的数据库连接池
		BasicDataSource source = new BasicDataSource();
		//设置基本信息
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl("jdbc:mysql://localhost:3306/test");
		source.setUsername("root");
		source.setPassword("root");
		//还可以设置其他涉及数据库连接池管理的相关属性：
		source.setInitialSize(10);
		source.setMaxActive(10);
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
	//方式二：使用配置文件（推荐）
	@Test
	public void	testGetConnection1() throws Exception{
		Properties pros = new Properties();
		//方式1:
		//InputStream is = ClassLoader.getSystemResourceAsStream("dbcp.properties");
		//方式2:
		FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
		pros.load(is);
		DataSource source = BasicDataSourceFactory.createDataSource(pros);
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
}
