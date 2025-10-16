package giaodienchuan.model.BackEnd.QuanLyNhanVien;

import giaodienchuan.model.BackEnd.ConnectionDB.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException; // Import này cần thiết
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class QuanLyNhanVienDAO {

    ConnectionDB qlnvConnection;

    public QuanLyNhanVienDAO() {
    }

    // Đọc dữ liệu từ bảng nhanvien
    public ArrayList<NhanVien> readDB() {
        ArrayList<NhanVien> dsnv = new ArrayList<>();
        qlnvConnection = new ConnectionDB();
        try {
            String qry = "SELECT * FROM nhanvien";
            ResultSet r = qlnvConnection.sqlQuery(qry);
            if (r != null) {
                while (r.next()) {
                    String manv = r.getString("MaNV");
                    String tennv = r.getString("TenNV");

                    // --- ĐIỀU CHỈNH QUAN TRỌNG Ở ĐÂY ---
                    // Đọc NgaySinh dưới dạng String trước
                    String ngaySinhStr = r.getString("NgaySinh");
                    LocalDate ngaysinh = null;
                    if (ngaySinhStr != null && !ngaySinhStr.trim().isEmpty()) {
                        try {
                            ngaysinh = LocalDate.parse(ngaySinhStr); // Chuyển đổi từ String sang LocalDate
                        } catch (DateTimeParseException e) {
                            System.err.println("Lỗi parse NgaySinh cho NV " + manv + ": " + ngaySinhStr + " - " + e.getMessage());
                            // Xử lý lỗi parse (ví dụ: gán null, hoặc giá trị mặc định)
                            // Hiện tại để null, bạn có thể thay đổi nếu cần.
                        }
                    }
                    // --- KẾT THÚC ĐIỀU CHỈNH ---

                    String diachi = r.getString("DiaChi");
                    String sdt = r.getString("SDT");
                    int tt = r.getInt("TrangThai");
                    dsnv.add(new NhanVien(manv, tennv, ngaysinh, diachi, sdt, tt));
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "-- ERROR! Lỗi đọc dữ liệu bảng nhân viên (SQL)\n" + ex.getMessage());
            ex.printStackTrace(); // In lỗi SQL ra console
        } catch (Exception ex) { // Bắt các lỗi Runtime khác (ví dụ: NullPointerException, IndexOutOfBoundsException, ...)
            JOptionPane.showMessageDialog(null, "-- ERROR! Lỗi đọc dữ liệu bảng nhân viên (Runtime)\n" + ex.getMessage());
            ex.printStackTrace(); // In lỗi Runtime ra console
        } finally {
            qlnvConnection.closeConnect();
        }
        return dsnv;
    }

    public Boolean add(NhanVien nv) {
        qlnvConnection = new ConnectionDB();
        Boolean ok = qlnvConnection.sqlUpdate("INSERT INTO nhanvien (MaNV, TenNV, NgaySinh, DiaChi, SDT, TrangThai) VALUES ('"
                + nv.getMaNV() + "', '" + nv.getTenNV() + "', '" + nv.getNgaySinh() + "', '" + nv.getDiaChi() + "', '" + nv.getSDT() + "', " + nv.getTrangThai() + ");");
        qlnvConnection.closeConnect();
        return ok;
    }

    public Boolean delete(String manv) {
        qlnvConnection = new ConnectionDB();
        Boolean ok = qlnvConnection.sqlUpdate("DELETE FROM nhanvien WHERE MaNV = '" + manv + "'");
        qlnvConnection.closeConnect();
        return ok;
    }

    public Boolean update(String MaNV, String TenNV, LocalDate NgaySinh, String DiaChi, String SDT, int trangthai) {
        qlnvConnection = new ConnectionDB();
        Boolean ok = qlnvConnection.sqlUpdate("UPDATE nhanvien SET "
                + "TenNV = '" + TenNV
                + "', NgaySinh = '" + NgaySinh
                + "', DiaChi = '" + DiaChi
                + "', SDT = '" + SDT
                + "', TrangThai = " + trangthai
                + " WHERE MaNV = '" + MaNV + "'");
        qlnvConnection.closeConnect();
        return ok;
    }

    public Boolean updateTrangThai(String manv, int trangthai) {
        qlnvConnection = new ConnectionDB();
        Boolean ok = qlnvConnection.sqlUpdate("UPDATE nhanvien SET "
                + "TrangThai = " + trangthai
                + " WHERE MaNV = '" + manv + "'");
        qlnvConnection.closeConnect();
        return ok;
    }
}