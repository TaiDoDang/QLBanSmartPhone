/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giaodienchuan.model.BackEnd.QuanLyPhieuNhap;

import giaodienchuan.model.BackEnd.ConnectionDB.ConnectionDB;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class QuanLyPhieuNhapDAO {

    ConnectionDB connection;

    public QuanLyPhieuNhapDAO() {
    }

    public ArrayList<PhieuNhap> readDB() {
        connection = new ConnectionDB();
        ArrayList<PhieuNhap> dspn = new ArrayList<>();
        try {
            String qry = "SELECT * FROM phieunhap";
            ResultSet rs = connection.sqlQuery(qry);
            if (rs != null) {
                while (rs.next()) {
                    PhieuNhap pn = new PhieuNhap();
                    pn.setMaPN(rs.getString(1));
                    pn.setMaNCC(rs.getString(2));
                    pn.setMaNV(rs.getString(3));

                    try {
                        String ngayStr = rs.getString("NgayNhap");
                        String gioStr  = rs.getString("GioNhap");

                        if (ngayStr != null && !ngayStr.isBlank()) {
                            pn.setNgayNhap(LocalDate.parse(ngayStr));
                        }
                        if (gioStr != null && !gioStr.isBlank()) {
                            pn.setGioNhap(LocalTime.parse(gioStr));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "-- ERROR! Lỗi parse ngày/giờ:\n" + ex.getMessage());
                    }


                    pn.setTongTien(rs.getFloat(6));
                    dspn.add(pn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "-- ERROR! Lỗi đọc dữ liệu bảng phieunhap:\n" + e.getMessage());
        } finally {
            connection.closeConnect();
        }
        return dspn;
    }


    public Boolean add(PhieuNhap pn) {
        connection = new ConnectionDB();
        Boolean ok = connection.sqlUpdate("INSERT INTO phieunhap(MaPN,MaNCC,MaNV,NgayNhap,GioNhap,TongTien) VALUES ('"
                + pn.getMaPN() + "','"
                + pn.getMaNCC() + "','"
                + pn.getMaNV() + "','"
                + pn.getNgayNhap() + "','"
                + pn.getGioNhap() + "','"
                + pn.getTongTien() + "');");
        connection.closeConnect();
        return ok;
    }

    public Boolean delete(String mapn) {
        connection = new ConnectionDB();
        Boolean ok = connection.sqlUpdate("DELETE FROM phieunhap WHERE MaPN='" + mapn + "';");
        if (!ok) {
            JOptionPane.showMessageDialog(null, "Vui lòng xóa hết chi tiết của phiếu nhập trước!");
        }
        connection.closeConnect();
        return ok;
    }


    public Boolean update(PhieuNhap pn) {
        connection = new ConnectionDB();
        Boolean ok = connection.sqlUpdate("UPDATE phieunhap SET "
                + "MaNCC='" + pn.getMaNCC()
                + "', MaNV='" + pn.getMaNV()
                + "', NgayNhap='" + pn.getNgayNhap()
                + "', GioNhap='" + pn.getGioNhap()
                + "', TongTien='" + pn.getTongTien()
                + "' WHERE MaPN='" + pn.getMaPN() + "';");
        connection.closeConnect();
        return ok;
    }

    public Boolean updateTongTien(String _mapn, float _tongTien) {
        connection = new ConnectionDB();
        Boolean ok = connection.sqlUpdate("UPDATE phieunhap SET TongTien='" + _tongTien + "' WHERE MaPN='" + _mapn + "';");
        connection.closeConnect();
        return ok;
    }

    public Boolean update(String maPN, String maNCC, String maNV, LocalDate ngayNhap, LocalTime gioNhap, float tongTien) {
        PhieuNhap pn = new PhieuNhap();
        pn.setMaPN(maPN);
        pn.setMaNCC(maNCC);
        pn.setMaNV(maNV);
        pn.setNgayNhap(ngayNhap);
        pn.setGioNhap(gioNhap);
        pn.setTongTien(tongTien);
        return update(pn);
    }

    public boolean exists(String maPN) {
        connection = new ConnectionDB();
        ResultSet rs = connection.sqlQuery("SELECT * FROM phieunhap WHERE MaPN='" + maPN + "'");
        boolean exists = false;
        try {
            exists = rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.closeConnect();
        return exists;
    }

}
