package giaodienchuan.model.BackEnd.QuanLyNCC;

import giaodienchuan.model.BackEnd.ConnectionDB.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class QuanLyNhaCungCapDAO {

    ConnectionDB qlnccConnection;

    public QuanLyNhaCungCapDAO() {

    }

    public ArrayList<NhaCungCap> readDB() {
        qlnccConnection = new ConnectionDB();
        ArrayList<NhaCungCap> dsncc = new ArrayList<>();
        try {
            String qry = "SELECT * FROM nhacungcap";
            ResultSet r = qlnccConnection.sqlQuery(qry);
            if (r != null) {
                while (r.next()) {
                    String mancc = r.getString("MaNCC");
                    String tenncc = r.getString("TenNCC");
                    String diachi = r.getString("DiaChi");
                    String sdt = r.getString("SDT");
                    String fax = r.getString("Fax");
                    dsncc.add(new NhaCungCap(mancc, tenncc, diachi, sdt, fax));
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "-- ERROR! Lỗi đọc dữ liệu bảng nhà cung cấp");
        } finally {
            qlnccConnection.closeConnect();
        }
        return dsncc;
    }

    public ArrayList<NhaCungCap> search(String columnName, String value) {
        qlnccConnection = new ConnectionDB();
        ArrayList<NhaCungCap> dsncc = new ArrayList<>();

        try {
            String qry = "SELECT * FROM nhacungcap WHERE " + columnName + " LIKE '%" + value + "%'";
            ResultSet r = qlnccConnection.sqlQuery(qry);
            if (r != null) {
                while (r.next()) {
                    String mancc = r.getString("MaNCC");
                    String tenncc = r.getString("TenNCC");
                    String diachi = r.getString("DiaChi");
                    String sdt = r.getString("SDT");
                    String fax = r.getString("Fax");
                    dsncc.add(new NhaCungCap(mancc, tenncc, diachi, sdt, fax));
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "-- ERROR! Lỗi tìm dữ liệu " + columnName + " = " + value + " bảng nhà cung cấp");
        } finally {
            qlnccConnection.closeConnect();
        }

        return dsncc;
    }

    public Boolean add(NhaCungCap ncc) {
        qlnccConnection = new ConnectionDB();
        Boolean ok = qlnccConnection.sqlUpdate("INSERT INTO nhacungcap (MaNCC, TenNCC, DiaChi, SDT, Fax) VALUES ('"
                + ncc.getMaNCC() + "', '"
                + ncc.getTenNCC() + "', '"
                + ncc.getDiaChi() + "', '"
                + ncc.getSDT() + "', '"
                + ncc.getFax() + "');");
        qlnccConnection.closeConnect();
        return ok;
    }

    public Boolean delete(String mancc) {
        qlnccConnection = new ConnectionDB();
        Boolean ok = qlnccConnection.sqlUpdate("DELETE FROM nhacungcap WHERE MaNCC = '" + mancc + "'");
        qlnccConnection.closeConnect();
        return ok;
    }

    public Boolean update(String MaNCC, String TenNCC, String DiaChi, String SDT, String Fax) {
        qlnccConnection = new ConnectionDB();
        Boolean ok = qlnccConnection.sqlUpdate("UPDATE nhacungcap SET "
                + "TenNCC = '" + TenNCC
                + "', DiaChi = '" + DiaChi
                + "', SDT = '" + SDT
                + "', Fax = '" + Fax
                + "' WHERE MaNCC = '" + MaNCC + "'");
        qlnccConnection.closeConnect();
        return ok;
    }

    public void close() {
        qlnccConnection.closeConnect();
    }
}
