package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConfig {

	public static Connection getConnection() {
		String url = "jdbc:mysql://localhost:3307/crm_app"; //Localhost là docker, Port 3307: Port Pushlish của máy tính, crm-app tên dự án
		String user = "root";
		String password = "admin123";
		Connection connection= null;
		// Cố gắng kết nối
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); //Khai báo dirver để điều khiển mysql
			connection = DriverManager.getConnection(url, user, password); //Mở kết nối cơ sở dũ liệu
		} catch (SQLException e) {
			System.out.println("Kết nối thất bại!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
}
