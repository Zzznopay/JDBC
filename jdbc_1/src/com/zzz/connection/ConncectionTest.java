package com.zzz.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class ConncectionTest {
	//方式一：
	@Test
	public void testConnection1() throws SQLException{
		//获取Driver实现类对象
		Driver driver = new com.mysql.jdbc.Driver();
		//url:http://localhost:8080/gmall/keyboard.jpg
		//jdbc.mysql:协议
		//localhost:ip地址
		//3306:默认mysql的端口号
		//test:test数据库
		String url = "jdbc:mysql://localhost:3306/test";
		//将用户和密码封装在Properties中
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password","root");
		Connection conn= driver.connect(url, info);
		System.out.println(conn);
	}
	//方式二:对方式一的迭代:在如下的程序中不出现第三方的api，使程序具有更好的可移植性
	@Test
	public void testConnection2() throws Exception{
		//获取Driver实现类对象:使用反射
		Class class1 = Class.forName("com.mysql.jdbc.Driver");
		
		Driver driver = (Driver)class1.newInstance();
		
		//提供要连接的数据库
		String url = "jdbc:mysql://localhost:3306/test";
		
		//提供连接需要的用户名和密码
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password","root");
		
		//获取连接
		Connection conn= driver.connect(url, info);
		System.out.println(conn);
	}
	//方式三:使用DriverManager替换Driver
	@Test
	public void testConnection3() throws Exception{		
		//提供另外三个连接的基本信息
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "root";
		//获取Driver实现类对象
		Class class1 = Class.forName("com.mysql.jdbc.Driver");		
		Driver driver = (Driver)class1.newInstance();
		//注册驱动
		DriverManager.registerDriver(driver);
		
		//获取连接
		Connection conn = DriverManager.getConnection(url,user,password);
		System.out.println(conn);
	}
	//方式四:可以只是加载驱动，不用显示注册驱动过了
	@Test
	public void testConnection4() throws Exception{
		//提供三个连接的基本信息
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "root";
		//加载驱动
		Class.forName("com.mysql.jdbc.Driver");	
		//相对于方式三，可以省略以下操作
//		Driver driver = (Driver)class1.newInstance();
//		注册驱动
//		DriverManager.registerDriver(driver);
		/*可以注释掉上述代码的原因，是因为在mysql的Driver类中声明有：
		 * static {
		 *      try {
		 *          java.sql.DriverManager.registerDriver(new Driver());
		 *      } catch (SQLException E) {
		 *          throw new RuntimeException("Can't register driver!");
		 *      }
		 *   }
		 */
		 	
		//获取连接
		Connection conn = DriverManager.getConnection(url,user,password);
		System.out.println(conn);
		}
	//方式五(最终版):将数据库连接需要的四个信息声明在配置文件中，通过读取配置文件的方式获取连接
	/*
	 * 1.实现了代码和数据的分离，实现了解耦。
	 * 2.如果需要修改配置信息，直接在配置文件中修改，不需要深入代码 
	 * 3.如果修改了配置信息，省去重新编译的过程。
	 */
	@Test
	public void testConnection5() throws Exception{
		//读取配置文件中的四个配置信息
		InputStream is = ConncectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		System.out.println(is);
		pros.load(is);
		is.close();
		
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		
		//加载驱动
		Class.forName(driverClass);	
		
		//获取连接
		Connection conn = DriverManager.getConnection(url,user,password);
		System.out.println(conn);
	}
	
	
	
}
