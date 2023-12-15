package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("hibernate")
public class HibernateProperties {

	private String driverClass = "com.mysql.cj.jdbc.Driver";
    private String host = "localhost";
    private String port = "3306";
    private String database = "test";
    private String user = "root";
    private String password = "root";

    private String hbm2ddlAuto = "validate";
    private String dialect = "org.hibernate.dialect.MySQLDialect";
    private String storageEngine = "innodb";
    private boolean showSql = false;
    
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHbm2ddlAuto() {
		return hbm2ddlAuto;
	}
	public void setHbm2ddlAuto(String hbm2ddlAuto) {
		this.hbm2ddlAuto = hbm2ddlAuto;
	}
	public String getDialect() {
		return dialect;
	}
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	public String getStorageEngine() {
		return storageEngine;
	}
	public void setStorageEngine(String storageEngine) {
		this.storageEngine = storageEngine;
	}
	public boolean isShowSql() {
		return showSql;
	}
	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}
	
	@Override
	public String toString() {
		return "HibernateProperties [driverClass=" + driverClass + ", host=" + host + ", port=" + port + ", database="
				+ database + ", user=" + user + ", password=" + password + ", hbm2ddlAuto=" + hbm2ddlAuto + ", dialect="
				+ dialect + ", storageEngine=" + storageEngine + ", showSql=" + showSql + "]";
	}
	
}
