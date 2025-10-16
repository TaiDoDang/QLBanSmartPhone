package giaodienchuan.model.BackEnd.ConnectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ConnectionDB {

    static int countConnection = 0;
    static int countQuery = 0;
    static int countUpdate = 0;

    Connection conn = null;
    Statement stmt = null;
    ResultSet rset = null;

    String DB_Path = null; // Thay đổi từ DB_Name thành DB_Path để lưu đường dẫn file SQLite
    String user_Name = null; // Không dùng cho SQLite, nhưng giữ lại cho constructor nếu muốn
    String pass = null; // Không dùng cho SQLite, nhưng giữ lại cho constructor nếu muốn

    // Trong SQLite, không cần serverName và port
    // String serverName = "acer3\\SQLEXPRESS";
    // int port = 1433;

    public ConnectionDB() {
        checkDriver();
        DB_Path = "D:/IdeaProjects/Database/OOPSQLQLDT.db"; // Đường dẫn tuyệt đối đến file DB SQLite
        // user_Name và pass không cần thiết cho SQLite
        setupConnect();
    }

    public ConnectionDB(String DB_Path) {
        checkDriver();
        this.DB_Path = DB_Path;
        setupConnect(); // Gọi setupConnect ngay khi khởi tạo với đường dẫn
    }

    public ConnectionDB(String DB_Path, String user_Name, String pass) {
        checkDriver();
        this.DB_Path = DB_Path;
        this.user_Name = user_Name; // Giữ lại cho tương thích constructor, nhưng không dùng
        this.pass = pass; // Giữ lại cho tương thích constructor, nhưng không dùng
        setupConnect();
    }

    // Kết nối tới DB
    private void setupConnect() {
        try {
            // URL kết nối cho SQLite
            String url = "jdbc:sqlite:" + DB_Path;
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            countConnection++;
            System.out.println("**\n" + countConnection + ": Success! Đã kết nối tới '" + DB_Path + "'");

        } catch (SQLException e) {
            System.err.println("-- ERROR! Không thể kết nối tới '" + DB_Path + "'");
            JOptionPane.showMessageDialog(null, "-- ERROR! Không thể kết nối tới '" + DB_Path + "'\n" + e.getLocalizedMessage());
        }
    }

    // Đăng nhập (thường không áp dụng cho SQLite bằng file, nhưng giữ lại để tương thích)
    public void logIn(String user_Name, String pass) {
        this.user_Name = user_Name;
        this.pass = pass;
        setupConnect(); // Thiết lập lại kết nối với thông tin mới (nếu có DB yêu cầu)
    }

    // Lấy data theo câu query
    public ResultSet sqlQuery(String qry) {
        if (checkConnect()) {
            try {
                rset = stmt.executeQuery(qry);
                countQuery++;
                System.out.println(countQuery + ": Success Query! " + qry);
                return rset;

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "-- ERROR! Không thể lấy dữ liệu từ " + DB_Path + "\n" + ex.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }

    // Ghi data theo câu update
    public Boolean sqlUpdate(String qry) {
        if (checkConnect()) {
            try {
                stmt.executeUpdate(qry);
                countUpdate++;
                System.out.println(countUpdate + ": Success Update! " + qry);
                return true;

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "-- ERROR! Không thể ghi dữ liệu xuống " + DB_Path + "\n" + ex.getLocalizedMessage());
                return false;
            }
        }
        return false;
    }

    // Đóng connection
    public void closeConnect() {
        try {
            if (conn != null) {
                conn.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            System.out.println("Success! Đóng kết nối tới '" + DB_Path + "' thành công.\n**");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "-- ERROR! Không thể đóng kết nối tới " + DB_Path + "\n" + ex.getLocalizedMessage());
        }
    }

    // Kiểm tra kết nối
    public Boolean checkConnect() {
        if (conn == null || stmt == null) {
            JOptionPane.showMessageDialog(null, "-- ERROR! Chưa thiết lập kết nối tới '" + DB_Path + "'. Vui lòng đăng nhập để thiết lập kết nối!");
            return false;
        }
        return true;
    }

    // Kiểm tra driver
    private void checkDriver() {
        try {
            // Driver cho SQLite
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "-- ERROR! Không tìm thấy Driver SQLite\n" + e.getLocalizedMessage());
        }
    }

    // Lấy headers của table
    public ArrayList<String> getHeaders(String tableName) {
        ArrayList<String> headers = new ArrayList<>();
        if (checkConnect()) {
            try {
                // SQLite không hỗ trợ WHERE 1 = 0 để lấy metadata dễ dàng như SQL Server
                // Cần đảm bảo bảng có ít nhất một hàng hoặc truy vấn thông tin schema từ sqlite_master
                // Cách an toàn hơn là vẫn lấy từ ResultSetMetaData nhưng với một truy vấn hợp lệ
                ResultSetMetaData rsMetaData = sqlQuery("SELECT * FROM " + tableName + " LIMIT 1").getMetaData();
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    headers.add(rsMetaData.getColumnName(i));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "-- ERROR! Không thể lấy headers của " + tableName + " trong " + DB_Path + "\n" + e.getLocalizedMessage());
            }
        }
        return headers;
    }
}