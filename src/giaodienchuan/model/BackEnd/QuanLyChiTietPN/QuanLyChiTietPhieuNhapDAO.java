package giaodienchuan.model.BackEnd.QuanLyChiTietPN;

import giaodienchuan.model.BackEnd.ConnectionDB.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class QuanLyChiTietPhieuNhapDAO {

    ConnectionDB qlctpnConnection;

    // Đọc dữ liệu từ bảng chitietphieunhap
    public ArrayList<ChiTietPhieuNhap> readDB() {
        ArrayList<ChiTietPhieuNhap> dsctpn = new ArrayList<>();
        qlctpnConnection = new ConnectionDB();
        try {
            String query = "SELECT * FROM chitietphieunhap";
            ResultSet r = qlctpnConnection.sqlQuery(query); // Trực tiếp sử dụng sqlQuery()
            if (r != null) {
                while (r.next()) {
                    String ma = r.getString("MaPN");
                    String maSP = r.getString("MaSP");
                    Integer soLuong = r.getInt("SoLuong");
                    Float donGia = r.getFloat("DonGia");

                    ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap(ma, maSP, soLuong, donGia);
                    dsctpn.add(ctpn);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Không thể đọc dữ liệu từ bảng chi tiết phiếu nhập");
        } finally {
            qlctpnConnection.closeConnect(); // Đảm bảo đóng kết nối
        }
        return dsctpn;
    }

    // Tìm kiếm chi tiết phiếu nhập theo cột
    public ArrayList<ChiTietPhieuNhap> search(String columnName, String value) {
        ArrayList<ChiTietPhieuNhap> dsctpn = new ArrayList<>();
        qlctpnConnection = new ConnectionDB();
        try {
            String query = "SELECT * FROM chitietphieunhap WHERE " + columnName + " LIKE '%" + value + "%'";
            ResultSet r = qlctpnConnection.sqlQuery(query); // Trực tiếp sử dụng sqlQuery()
            if (r != null) {
                while (r.next()) {
                    String ma = r.getString("MaPN");
                    String maSP = r.getString("MaSP");
                    Integer soLuong = r.getInt("SoLuong");
                    Float donGia = r.getFloat("DonGia");

                    ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap(ma, maSP, soLuong, donGia);
                    dsctpn.add(ctpn);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Không thể tìm dữ liệu theo điều kiện " + columnName + " = " + value);
        } finally {
            qlctpnConnection.closeConnect(); // Đảm bảo đóng kết nối
        }
        return dsctpn;
    }

    // Thêm chi tiết phiếu nhập mới
    public boolean add(ChiTietPhieuNhap ctpn) {
        qlctpnConnection = new ConnectionDB();
        Boolean ok = qlctpnConnection.sqlUpdate("INSERT INTO chitietphieunhap(MaPN, MaSP, SoLuong, DonGia) VALUES ('"
                + ctpn.getMa() + "', '"
                + ctpn.getMaSP() + "', '"
                + ctpn.getSoLuong() + "', '"
                + ctpn.getDonGia() + "')");
        qlctpnConnection.closeConnect(); // Đảm bảo đóng kết nối
        return ok;
    }

    // Xóa tất cả chi tiết phiếu nhập theo mã phiếu nhập
    public boolean deleteAll(String mapn) {
        qlctpnConnection = new ConnectionDB();
        boolean success = qlctpnConnection.sqlUpdate("DELETE FROM chitietphieunhap WHERE MaPN = '" + mapn + "'");
        qlctpnConnection.closeConnect(); // Đảm bảo đóng kết nối
        return success;
    }

    // Xóa chi tiết phiếu nhập theo mã phiếu nhập và mã sản phẩm
    public boolean delete(String mapn, String masp) {
        qlctpnConnection = new ConnectionDB();
        boolean success = qlctpnConnection.sqlUpdate("DELETE FROM chitietphieunhap WHERE MaPN = '" + mapn + "' AND MaSP = '" + masp + "'");
        qlctpnConnection.closeConnect(); // Đảm bảo đóng kết nối
        return success;
    }

    // Cập nhật chi tiết phiếu nhập
    public boolean update(String mapn, String masp, int soluong, float dongia) {
        qlctpnConnection = new ConnectionDB();
        boolean ok = qlctpnConnection.sqlUpdate("UPDATE chitietphieunhap SET SoLuong = '" + soluong
                + "', DonGia = '" + dongia
                + "' WHERE MaPN = '" + mapn + "' AND MaSP = '" + masp + "'");
        qlctpnConnection.closeConnect(); // Đảm bảo đóng kết nối
        return ok;
    }
}
