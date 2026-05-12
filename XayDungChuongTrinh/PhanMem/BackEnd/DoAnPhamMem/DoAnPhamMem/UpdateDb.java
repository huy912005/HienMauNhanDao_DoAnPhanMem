import java.sql.*;

public class UpdateDb {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/QuanLyHienMauDN?useSSL=false&allowPublicKeyRetrieval=true",
                "root", "12345");
            Statement stmt = conn.createStatement();
            try { stmt.execute("SET check_constraint_checks = 0;"); } catch(Exception ignored) {}
            int updated = stmt.executeUpdate("UPDATE dondangky d JOIN ketqualamsang k ON d.maDon = k.maDon SET d.trangThai = 'Đã khám' WHERE d.trangThai = 'Đã hiến' AND k.ketQua = 1");
            System.out.println("Rows updated: " + updated);
            int deleted = stmt.executeUpdate("DELETE FROM tuimau");
            System.out.println("TuiMau deleted: " + deleted);
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
