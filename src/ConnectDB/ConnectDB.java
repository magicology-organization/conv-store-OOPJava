/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectDB;

/**
 *
 * @author ADMIN
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Đường dẫn kết nối
            String url = "jdbc:sqlserver://localhost:1433;databaseName=QLCHTL;encrypt=false";
            String user = "sa";       // user SQL Server
            String password = "sapassword"; // password SQL Server

            // Đăng ký driver và tạo kết nối
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối: " + e.getMessage());
        }
        return conn;
    }
}

