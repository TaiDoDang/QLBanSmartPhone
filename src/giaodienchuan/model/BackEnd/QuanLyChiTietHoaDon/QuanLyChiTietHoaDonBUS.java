//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package giaodienchuan.model.BackEnd.QuanLyChiTietHoaDon;

import giaodienchuan.model.BackEnd.QuanLyHoaDon.QuanLyHoaDonBUS;
import giaodienchuan.model.BackEnd.QuanLySanPham.QuanLySanPhamBUS;
import giaodienchuan.model.BackEnd.QuanLySanPham.SanPham;
import java.util.ArrayList;

public class QuanLyChiTietHoaDonBUS {
    ArrayList<ChiTietHoaDon> dscthd = new ArrayList();
    private QuanLyChiTietHoaDonDAO qlcthdDAO = new QuanLyChiTietHoaDonDAO();
    private QuanLyHoaDonBUS qlhdBUS = new QuanLyHoaDonBUS();
    private QuanLySanPhamBUS qlspBUS = new QuanLySanPhamBUS();

    public QuanLyChiTietHoaDonBUS() {
        this.dscthd = this.qlcthdDAO.readDB();
    }

    public ArrayList<ChiTietHoaDon> getDscthd() {
        return this.dscthd;
    }

    public void readDB() {
        this.dscthd = this.qlcthdDAO.readDB();
    }

    public ChiTietHoaDon getChiTiet(String mahd, String masp) {
        for(ChiTietHoaDon ct : this.dscthd) {
            if (ct.getMaSanPham().equals(masp) && ct.getMaHoaDon().equals(mahd)) {
                return ct;
            }
        }

        return null;
    }

    public ArrayList<ChiTietHoaDon> getAllChiTiet(String mahd) {
        ArrayList<ChiTietHoaDon> result = new ArrayList();

        for(ChiTietHoaDon ct : this.dscthd) {
            if (ct.getMaHoaDon().equals(mahd)) {
                result.add(ct);
            }
        }

        return result;
    }

    public Boolean add(ChiTietHoaDon ct) {
        int soLuongChiTietMoi = ct.getSoLuong();
        ArrayList<ChiTietHoaDon> toRemove = new ArrayList();
        int tongSoLuong = ct.getSoLuong();

        for(ChiTietHoaDon cthd : this.dscthd) {
            if (cthd.getMaHoaDon().equals(ct.getMaHoaDon()) && cthd.getMaSanPham().equals(ct.getMaSanPham())) {
                tongSoLuong += cthd.getSoLuong();
                toRemove.add(cthd);
            }
        }

        this.dscthd.removeAll(toRemove);
        this.qlcthdDAO.delete(ct.getMaHoaDon(), ct.getMaSanPham());
        ct.setSoLuong(tongSoLuong);
        Boolean success = this.qlcthdDAO.add(ct);
        if (success) {
            this.dscthd.add(ct);
            this.updateSoLuongSanPham(ct.getMaSanPham(), -soLuongChiTietMoi);
            return true;
        } else {
            return false;
        }
    }

    public Boolean add(String maHoaDon, String maSanPham, int soLuong, float donGia) {
        ChiTietHoaDon hd = new ChiTietHoaDon(maHoaDon, maSanPham, soLuong, donGia);
        return this.add(hd);
    }

    public Boolean update(String maHoaDon, String maSanPham, int soLuong, float donGia) {
        ChiTietHoaDon hd = new ChiTietHoaDon(maHoaDon, maSanPham, soLuong, donGia);
        return this.update(hd);
    }

    public Boolean update(ChiTietHoaDon hd) {
        Boolean success = this.qlcthdDAO.update(hd);
        if (success) {
            for(ChiTietHoaDon cthd : this.dscthd) {
                if (cthd.getMaHoaDon().equals(hd.getMaHoaDon())) {
                    ;
                }
            }
        }

        return success;
    }

    private Boolean updateSoLuongSanPham(String _masp, int _soLuongThayDoi) {
        for(SanPham sp : this.qlspBUS.getDssp()) {
            if (sp.getMaSP().equals(_masp)) {
                return this.qlspBUS.updateSoLuong(_masp, sp.getSoLuong() + _soLuongThayDoi);
            }
        }

        return false;
    }

    public Boolean delete(String _maHoaDon, String _maSanPham) {
        Boolean success = this.qlcthdDAO.delete(_maHoaDon, _maSanPham);
        if (success) {
            for(ChiTietHoaDon cthd : this.dscthd) {
                if (cthd.getMaHoaDon().equals(_maHoaDon) && cthd.getMaSanPham().equals(_maSanPham)) {
                    this.updateSoLuongSanPham(_maSanPham, cthd.getSoLuong());
                    this.dscthd.remove(cthd);
                    return true;
                }
            }
        }

        return false;
    }

    public Boolean deleteAll(String _maHoaDon) {
        Boolean success = this.qlcthdDAO.deleteAll(_maHoaDon);
        if (success) {
            for(ChiTietHoaDon cthd : this.dscthd) {
                if (cthd.getMaHoaDon().equals(_maHoaDon)) {
                    this.dscthd.remove(cthd);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ChiTietHoaDon> search(String type, String keyword, int soLuong1, int soLuong2, float thanhTien1, float thanhTien2) {
        ArrayList<ChiTietHoaDon> result = new ArrayList();
        this.dscthd.forEach((hd) -> {
            switch (type) {
                case "Tất cả":
                    if (hd.getMaHoaDon().toLowerCase().contains(keyword.toLowerCase()) || hd.getMaSanPham().toLowerCase().contains(keyword.toLowerCase()) || String.valueOf(hd.getSoLuong()).toLowerCase().contains(keyword.toLowerCase()) || String.valueOf(hd.getDonGia()).toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;
                case "Mã hóa đơn":
                    if (hd.getMaHoaDon().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;
                case "Mã sản phẩm":
                    if (hd.getMaSanPham().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;
                case "Số lượng":
                    if (String.valueOf(hd.getSoLuong()).toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;
                case "Đơn giá":
                    if (String.valueOf(hd.getDonGia()).toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
            }

        });

        for(int i = result.size() - 1; i >= 0; --i) {
            ChiTietHoaDon ct = (ChiTietHoaDon)result.get(i);
            int sl = ct.getSoLuong();
            float tt = ct.getDonGia() * (float)sl;
            Boolean soLuongKhongThoa = soLuong1 != -1 && sl < soLuong1 || soLuong2 != -1 && sl > soLuong2;
            Boolean donGiaKhongThoa = thanhTien1 != -1.0F && tt < thanhTien1 || thanhTien2 != -1.0F && tt > thanhTien2;
            if (soLuongKhongThoa || donGiaKhongThoa) {
                result.remove(ct);
            }
        }

        return result;
    }
}