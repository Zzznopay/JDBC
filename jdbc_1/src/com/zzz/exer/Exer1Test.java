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
		System.out.print("�û���:");
		String name = scanner.next();
		System.out.print("����:");
		String email = scanner.next();
		System.out.print("����:");
		String birth = scanner.next();
		String sql = "insert into customers (name,email,birth) values (?,?,?)";
		int insertCount = update(sql,name,email,birth);
		if(insertCount > 0){
			System.out.println("��ӳɹ�");
		}else{
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
}
