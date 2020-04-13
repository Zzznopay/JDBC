package com.zzz.exer;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import org.junit.Test;

import com.zzz.bean.Student;
import com.zzz.utils.JDBCUtils;

public class Exer2Test {
	/*
	 * 向examstudent表中添加数据
	 * Type: 
	 * IDCard: 
	 * ExamCard: 
	 * StudentName: 
	 * Location: 
	 * Grade:
	 */
	@Test
	public void testInsert(){
		Scanner scanner = new Scanner(System.in);
		System.out.print("四级/六级:");
		int type = scanner .nextInt();
		System.out.print("身份证号:");
		String IDCard = scanner.next();
		System.out.print("准考证号:");
		String examCard = scanner.next();
		System.out.print("学生姓名:");
		String studentName = scanner.next();
		System.out.print("所在城市:");
		String location = scanner.next();
		System.out.print("考试成绩:");
		int grade = scanner .nextInt();
		
		String sql = "insert into examstudent(type,IDCard,examCard,studentName,location,grade)values(?,?,?,?,?,?)";
		int insertCount = update(sql,type,IDCard,examCard,studentName,location,grade);
		if(insertCount > 0){
			System.out.println("添加成功");
		}else {
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
	//根据身份证号或者准考证号查询学生成绩信息
	@Test
	public void queryWithIDCardOrExamcardTest(){
		System.out.println("请输入你要输入的类型:");
		System.out.println("a.身份证号");
		System.out.println("b.准考证号");
		Scanner scanner = new Scanner(System.in);
		String selection = scanner.next();
		if("a".equalsIgnoreCase(selection)){//if(selection.equalsIgnoreCase("a"))
			 System.out.println("请输入身份证号:");
			 String IDCard = scanner.next();
			 String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName studentName,Location location,Grade grade from examstudent where IDCard = ?";			 
			 Student student = getInstance(Student.class, sql, IDCard);
			 
			 if(student != null){
				System.out.println(student);
			 }else {
				System.out.println("输入身份证号有误！");
			}
		}else if("b".equalsIgnoreCase(selection)){
			 System.out.println("请输入准考证号:");
			 String examCard = scanner.next();
			 String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName studentName,Location location,Grade grade from examstudent where examCard = ?";			 
			 Student student = getInstance(Student.class, sql, examCard);
			 
			 if(student != null){
				System.out.println(student);
			 }else {
				System.out.println("输入准考证号有误！");
			}
		}else{
			System.out.println("你输入有误，请重新进入程序");
		}
	}
	public <T> T getInstance(Class<T> clazz,String sql,Object ...args){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//小心参数声明
			}
			rs = ps.executeQuery();
			//获取结果集的元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			//通过ResultSetMetaData获取结果集的列数
			int columnCount = rsmd.getColumnCount();
			if(rs.next()){
				T t = clazz.newInstance();
				//处理结果集一行数据中的每一列
				for(int i = 0;i < columnCount;i ++){
					//获取列值
					Object columnValue = rs.getObject(i + 1);
					//获取每个列的列名
//					String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					//给t对象指定columnName属性，赋值为columnValue:通过反射
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t,columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps, rs);
		}		
		return null;
	}
	//删除指定学生信息
	@Test
	public void testDeleteByExamCard(){
		System.out.println("请输入学生的准考证号:");
		Scanner scanner = new Scanner(System.in);
		String examCard = scanner.next();
		//查询指定准考证号的学生
		String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName studentName,Location location,Grade grade from examstudent where examCard = ?";
		Student student = getInstance(Student.class, sql, examCard); 
		if(student == null){
			System.out.println("查无此人，请重新输入！");
		}else{
			String sql1 = "delete from examstudent where examCard = ?";
			int deleteCount = update(sql1,examCard);
			
			if(deleteCount > 0){
				System.out.println("删除成功");
			}						
		}
	} 
	//优化后
	@Test
	public void testDeleteByExamCard1(){
		System.out.println("请输入学生的准考证号:");
		Scanner scanner = new Scanner(System.in);
		String examCard = scanner.next();
		String sql = "delete from examstudent where examCard = ?";
		int deleteCount = update(sql,examCard);
		if(deleteCount > 0){
			System.out.println("删除成功");
		}else{
			System.out.println("查无此人，请重新输入！");
		}
	}
}
