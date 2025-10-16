package giaodienchuan.model.BackEnd.ReadWriteFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.EOFException; // Import EOFException
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class WorkWithFile {

    String urlFile;

    public WorkWithFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public void write(String s) {
        try (DataOutputStream os = new DataOutputStream(new FileOutputStream(this.urlFile, false))) {
            os.writeUTF(s);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorkWithFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WorkWithFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String read() {
        String result = "";
        try (DataInputStream os = new DataInputStream(new FileInputStream(this.urlFile))) {
            result += os.readUTF();

        } catch (EOFException ex) { // BẮT RIÊNG EOFException
            // Đây là trường hợp tệp trống, không cần log là SEVERE
            // Chỉ cần trả về chuỗi rỗng
            System.out.println("File " + this.urlFile + " is empty or contains no valid UTF data."); // In thông báo nhỏ (tùy chọn)
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WorkWithFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WorkWithFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}