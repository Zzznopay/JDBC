package com.zzz.transaction;

import java.sql.Connection;

import org.junit.Test;

import com.zzz.utils.JDBCUtils;

public class ConnectionTest {
	@Test
	public void testGetConnection() throws Exception{
		Connection conn = JDBCUtils.getConnection();
		System.out.println(conn);
	}
}
