package giaodienchuan.model.BackEnd.QuanLyHoaDon;

import giaodienchuan.model.BackEnd.ConnectionDB.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class QuanLyHoaDonDAO {

    // Định dạng ngày và giờ ở dạng ISO (yyyy-MM-dd / HH:mm:ss)
    private static final DateTimeFormatter DATE_FMT  = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FMT  = DateTimeFormatter.ISO_LOCAL_TIME;

    private ConnectionDB connection;

    /*----------------------------------- ĐỌC DỮ LIỆU -----------------------------------*/
    public ArrayList<HoaDon> readDB() {
        connection = new ConnectionDB();
        ArrayList<HoaDon> dshd = new ArrayList<>();

        try {
            String qry = "SELECT * FROM hoadon";
            ResultSet rs = connection.sqlQuery(qry);

            if (rs != null) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHoaDon   (rs.getString("MaHD"));
                    hd.setMaNhanVien (rs.getString("MaNV"));
                    hd.setMaKhachHang(rs.getString("MaKH"));
                    hd.setMaKhuyenMai(rs.getString("MaKM"));

                    /* NgayLap & GioLap lưu TEXT → getString + parse */
                    String ngayLapStr = rs.getString("NgayLap");
                    if (ngayLapStr != null && !ngayLapStr.isEmpty()) {
                        hd.setNgayLap(LocalDate.parse(ngayLapStr, DATE_FMT));
                    }

                    String gioLapStr  = rs.getString("GioLap");
                    if (gioLapStr != null && !gioLapStr.isEmpty()) {
                        hd.setGioLap(LocalTime.parse(gioLapStr, TIME_FMT));
                    }

                    hd.setTongTien(rs.getFloat("TongTien"));
                    dshd.add(hd);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Không đọc được dữ liệu bảng hóa đơn!!");
        } finally {
            connection.closeConnect();
        }
        return dshd;
    }

    /*----------------------------------- THÊM HÓA ĐƠN ----------------------------------*/
    public boolean add(HoaDon hd) {
        connection = new ConnectionDB();

        // Chuyển ngày & giờ sang chuỗi ISO trước khi chèn
        String ngayLapStr = hd.getNgayLap() != null ? hd.getNgayLap().format(DATE_FMT) : "";
        String gioLapStr  = hd.getGioLap()  != null ? hd.getGioLap().format(TIME_FMT) : "";

        boolean success = connection.sqlUpdate(
                "INSERT INTO hoadon (MaHD, MaNV, MaKH, MaKM, NgayLap, GioLap, TongTien) VALUES ('"
                        + hd.getMaHoaDon()   + "','"
                        + hd.getMaNhanVien() + "','"
                        + hd.getMaKhachHang()+ "','"
                        + hd.getMaKhuyenMai()+ "','"
                        + ngayLapStr         + "','"
                        + gioLapStr          + "','"
                        + hd.getTongTien()   + "');"
        );
        connection.closeConnect();
        return success;
    }

    /*----------------------------------- XÓA HÓA ĐƠN -----------------------------------*/
    public boolean delete(String mahd) {
        connection = new ConnectionDB();
        if (!connection.sqlUpdate("DELETE FROM hoadon WHERE MaHD='" + mahd + "';")) {
            JOptionPane.showMessageDialog(null, "Vui lòng xoá hết chi tiết của hoá đơn trước!");
            connection.closeConnect();
            return false;
        }
        connection.closeConnect();
        return true;
    }

    /*-------------------------------- CẬP NHẬT HÓA ĐƠN ---------------------------------*/
    public boolean update(HoaDon hd) {
        connection = new ConnectionDB();

        String ngayLapStr = hd.getNgayLap() != null ? hd.getNgayLap().format(DATE_FMT) : "";
        String gioLapStr  = hd.getGioLap()  != null ? hd.getGioLap().format(TIME_FMT) : "";

        boolean success = connection.sqlUpdate(
                "UPDATE hoadon SET "
                        + "MaNV='"      + hd.getMaNhanVien()
                        + "', MaKH='"   + hd.getMaKhachHang()
                        + "', MaKM='"   + hd.getMaKhuyenMai()
                        + "', NgayLap='" + ngayLapStr
                        + "', GioLap='"  + gioLapStr
                        + "', TongTien='" + hd.getTongTien()
                        + "' WHERE MaHD='" + hd.getMaHoaDon() + "';"
        );
        connection.closeConnect();
        return success;
    }

    /*------------------------- CHỈ CẬP NHẬT TỔNG TIỀN --------------------------*/
    public boolean updateTongTien(String mahd, float tongTien) {
        connection = new ConnectionDB();
        boolean success = connection.sqlUpdate(
                "UPDATE hoadon SET TongTien='" + tongTien + "' WHERE MaHD='" + mahd + "';"
        );
        connection.closeConnect();
        return success;
    }

    /*----------- Tiện ích gọi nhanh update() từ các lớp khác ------------------*/
    public boolean update(String maHD, String maNV, String maKH, String maKM,
                          LocalDate ngayLap, LocalTime gioLap, float tongTien) {

        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(maHD);
        hd.setMaNhanVien(maNV);
        hd.setMaKhachHang(maKH);
        hd.setMaKhuyenMai(maKM);
        hd.setNgayLap(ngayLap);
        hd.setGioLap(gioLap);
        hd.setTongTien(tongTien);
        return update(hd);
    }
}
