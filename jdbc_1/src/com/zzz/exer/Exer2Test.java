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
	 * ��examstudent�����������
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
		System.out.print("�ļ�/����:");
		int type = scanner .nextInt();
		System.out.print("���֤��:");
		String IDCard = scanner.next();
		System.out.print("׼��֤��:");
		String examCard = scanner.next();
		System.out.print("ѧ������:");
		String studentName = scanner.next();
		System.out.print("���ڳ���:");
		String location = scanner.next();
		System.out.print("���Գɼ�:");
		int grade = scanner .nextInt();
		
		String sql = "insert into examstudent(type,IDCard,examCard,studentName,location,grade)values(?,?,?,?,?,?)";
		int insertCount = update(sql,type,IDCard,examCard,studentName,location,grade);
		if(insertCount > 0){
			System.out.println("��ӳɹ�");
		}else {
			System.out.println("���ʧ��");
		}		
	}		
	public int update(String sql,Object ...args) {//sqlռλ���ĸ�����ɱ��βεĳ�����ͬ
		//ͨ�õ���ɾ�Ĳ���
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//С�Ĳ�������
			}
			/* ִ��
			 * ps.execute();
			 * ���ִ�е��ǲ�ѯ�������з��ؽ�����򷵻�true��
			 * ���ִ�е�����ɾ�Ĳ�����û�з��ؽ�����򷵻�false��
			 */
//			��ʽһ:ps.execute();
			//��ʽ��:
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);	
		}
		return 0;
	}
	//�������֤�Ż���׼��֤�Ų�ѯѧ���ɼ���Ϣ
	@Test
	public void queryWithIDCardOrExamcardTest(){
		System.out.println("��������Ҫ���������:");
		System.out.println("a.���֤��");
		System.out.println("b.׼��֤��");
		Scanner scanner = new Scanner(System.in);
		String selection = scanner.next();
		if("a".equalsIgnoreCase(selection)){//if(selection.equalsIgnoreCase("a"))
			 System.out.println("���������֤��:");
			 String IDCard = scanner.next();
			 String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName studentName,Location location,Grade grade from examstudent where IDCard = ?";			 
			 Student student = getInstance(Student.class, sql, IDCard);
			 
			 if(student != null){
				System.out.println(student);
			 }else {
				System.out.println("�������֤������");
			}
		}else if("b".equalsIgnoreCase(selection)){
			 System.out.println("������׼��֤��:");
			 String examCard = scanner.next();
			 String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName studentName,Location location,Grade grade from examstudent where examCard = ?";			 
			 Student student = getInstance(Student.class, sql, examCard);
			 
			 if(student != null){
				System.out.println(student);
			 }else {
				System.out.println("����׼��֤������");
			}
		}else{
			System.out.println("���������������½������");
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
				ps.setObject(i+1, args[i]);//С�Ĳ�������
			}
			rs = ps.executeQuery();
			//��ȡ�������Ԫ����
			ResultSetMetaData rsmd = rs.getMetaData();
			//ͨ��ResultSetMetaData��ȡ�����������
			int columnCount = rsmd.getColumnCount();
			if(rs.next()){
				T t = clazz.newInstance();
				//��������һ�������е�ÿһ��
				for(int i = 0;i < columnCount;i ++){
					//��ȡ��ֵ
					Object columnValue = rs.getObject(i + 1);
					//��ȡÿ���е�����
//					String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					//��t����ָ��columnName���ԣ���ֵΪcolumnValue:ͨ������
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
	//ɾ��ָ��ѧ����Ϣ
	@Test
	public void testDeleteByExamCard(){
		System.out.println("������ѧ����׼��֤��:");
		Scanner scanner = new Scanner(System.in);
		String examCard = scanner.next();
		//��ѯָ��׼��֤�ŵ�ѧ��
		String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName studentName,Location location,Grade grade from examstudent where examCard = ?";
		Student student = getInstance(Student.class, sql, examCard); 
		if(student == null){
			System.out.println("���޴��ˣ����������룡");
		}else{
			String sql1 = "delete from examstudent where examCard = ?";
			int deleteCount = update(sql1,examCard);
			
			if(deleteCount > 0){
				System.out.println("ɾ���ɹ�");
			}						
		}
	} 
	//�Ż���
	@Test
	public void testDeleteByExamCard1(){
		System.out.println("������ѧ����׼��֤��:");
		Scanner scanner = new Scanner(System.in);
		String examCard = scanner.next();
		String sql = "delete from examstudent where examCard = ?";
		int deleteCount = update(sql,examCard);
		if(deleteCount > 0){
			System.out.println("ɾ���ɹ�");
		}else{
			System.out.println("���޴��ˣ����������룡");
		}
	}
}
