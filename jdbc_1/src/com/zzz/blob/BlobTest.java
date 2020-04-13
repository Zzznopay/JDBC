package com.zzz.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.Test;

import com.zzz.bean.Customer;
import com.zzz.utils.JDBCUtils;

/**   
*      
* @description 测试使用PreparedStatement操作Blob类型的数据
* @author zzz   
* @date 2020年4月10日 下午8:55:48   
* @version        
*/
public class BlobTest {
	//向数据表customers中插入Blob类型的字段
	@Test
	public void testInsert() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, "美女");
			ps.setObject(2, "meinv@qq.com");
			ps.setObject(3, "2000-01-01");
			FileInputStream is = new FileInputStream(new File("res/meinv.jpg"));
			ps.setBlob(4, is);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResources(conn, ps);
		} 		
	}
	//查询数据表customers中Blob类型的字段
	@Test
	public void testQuery() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "select id,name,email,birth,photo from customers where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, 16);
			
			rs = ps.executeQuery();
			if(rs.next()){
				//方式一
	//			int id = rs.getInt(1);
	//			String name = rs.getString(2);
	//			String email = rs.getString(3);
	//			Date birth = rs.getDate(4);
				//方式二
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date birth = rs.getDate("birth");
				Customer cust = new Customer(id,name,email,birth);
				System.out.println(cust);
				//将Blob类型的字段下载下来，以文件的方式保存在本地
				Blob photo = rs.getBlob("photo");
				is = photo.getBinaryStream();
				fos = new FileOutputStream("res/zhuyin.jpg");
				byte[] buffer = new byte[1024];
				int len;
				while((len = is.read(buffer)) != -1){
					fos.write(buffer,0,len);
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is != null){
					is.close();
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(fos != null){
					fos.close();
				}		
			} catch (IOException e) {
				e.printStackTrace();
			}
			JDBCUtils.closeResources(conn, ps, rs);
		}
		
	}
	
}
