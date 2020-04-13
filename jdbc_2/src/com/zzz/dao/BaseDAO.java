package com.zzz.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zzz.utils.JDBCUtils;

/*
 * DAO:data(base) access object
 * ��װ����������ݱ��ͨ�ò���
 */
public abstract class BaseDAO {
	//ͨ�õ���ɾ�Ĳ���-------version 2.0(����������)
	public int update(Connection conn,String sql,Object ...args) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			//���ռλ��
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//С�Ĳ�������
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//�޸���Ϊ�Զ��ύ���ݣ���Ҫ�����ʹ�����ݿ����ӳ�ʱʹ��
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCUtils.closeResources(null, ps);	
		}
		return 0;
	}
	//ͨ�õĲ�ѯ���������ڷ������ݱ��е�һ����¼(version 2.0:��������)
	public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
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
//						String columnName = rsmd.getColumnName(i + 1);
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
			JDBCUtils.closeResources(null, ps, rs);
		}		
		return null;
	}
	//ͨ�õĲ�ѯ���������ڷ������ݱ��еĶ�����¼���ɵļ���(version 2.0:��������)
	public <T> List<T> getForList(Connection conn,Class<T> clazz,String sql,Object ...args){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i+1, args[i]);//С�Ĳ�������
			}
			rs = ps.executeQuery();
			//��ȡ�������Ԫ����
			ResultSetMetaData rsmd = rs.getMetaData();
			//ͨ��ResultSetMetaData��ȡ�����������
			int columnCount = rsmd.getColumnCount();
			//�������϶���
			ArrayList<T> list = new ArrayList<T>();
			while(rs.next()){
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
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(null, ps, rs);
		}		
		return null;
	}
	//���ڲ�ѯ����ֵ��ͨ�÷���
	public <E> E getValue(Connection conn,String sql,Object ...args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 1;i <args.length;i ++){
				ps.setObject(i + 1,args[i]);
			}
			rs = ps.executeQuery();
			if(rs.next()){
				return (E) rs.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(null, ps, rs);
		}
		return null;
	} 
}
