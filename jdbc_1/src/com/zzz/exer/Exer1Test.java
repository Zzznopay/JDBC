package com.zzz.exer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import org.junit.Test;

import com.zzz.utils.JDBCUtils;

public class Exer1Test {
	@Test
	public void testInsert(){
		Scanner scanner = new Scanner(System.in);
		System.out.print("用户名:");
		String name = scanner.next();
		System.out.print("邮箱:");
		String email = scanner.next();
		System.out.print("生日:");
		String birth = scanner.next();
		String sql = "insert into customers (name,email,birth) values (?,?,?)";
		int insertCount = update(sql,name,email,birth);
		if(insertCount > 0){
			System.out.println("添加成功");
		}else{
			System.out.println("添加失败");
		}
		
		
	}
	
	public int update(String sql,Object ...args) {//sql占位符的个数与可变形参的长度相同
		//通用的增删改操作
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//小心参数声明
			}
			/* 执行
			 * ps.execute();
			 * 如果执行的是查询操作，有返回结果，则返回true，
			 * 如果执行的是增删改操作，没有返回结果，则返回false。
			 */
//			方式一:ps.execute();
			//方式二:
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);	
		}
		return 0;
	}
}
