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
 * @description ����DBCP�����ݿ����ӳؼ���
 * @return
 * @throws Exception
 */
public class DBCPTest {	
	//��ʽһ:���Ƽ�
	@Test
	public void testGetConnection() throws Exception{
		//������DBCP�����ݿ����ӳ�
		BasicDataSource source = new BasicDataSource();
		//���û�����Ϣ
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl("jdbc:mysql://localhost:3306/test");
		source.setUsername("root");
		source.setPassword("root");
		//���������������漰���ݿ����ӳع����������ԣ�
		source.setInitialSize(10);
		source.setMaxActive(10);
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
	//��ʽ����ʹ�������ļ����Ƽ���
	@Test
	public void	testGetConnection1() throws Exception{
		Properties pros = new Properties();
		//��ʽ1:
		//InputStream is = ClassLoader.getSystemResourceAsStream("dbcp.properties");
		//��ʽ2:
		FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
		pros.load(is);
		DataSource source = BasicDataSourceFactory.createDataSource(pros);
		Connection conn = source.getConnection();
		System.out.println(conn);
	}
}
