package com.ceair.activiti.spring.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;



public class DbUtil {

	private static DataSource dataSource;
	public static void setDataSource(DataSource dataSource) {
		DbUtil.dataSource = dataSource;
	}

	private DbUtil() {
		
	}
	
	public static <T> T getPojo(String sql, Class<T> clazz, Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		return runner.query(sql, new BeanHandler<T>(clazz), params);
	}
	
	public static <T> List<T> getPojoList(String sql, Class<T> clazz, Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		return runner.query(sql, new BeanListHandler<T>(clazz), params);
	}
	
	public static void update(String sql, Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		runner.update(sql, params);
	}
	
}