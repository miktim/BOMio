/*
 * BOM io test. MIT (c) 2022 miktim@mail.ru
 */
package bomio;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.miktim.io.BomFileReader;
import org.miktim.io.BomFileWriter;
import org.miktim.io.BomTable;

public class BomIoTest {

    public static void log(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) throws IOException {
        log("BOM IO Test\r\n");
        String fileName = "./BOMutf-8.txt";
        log("Read: " + fileName);
        BomFileReader br = new BomFileReader(fileName);
        log(br.getEncoding() + "/" + br.getEncoder());
        char[] cbuf = new char[128];
        br.read(cbuf, 0, 128);
        log(new String(cbuf));
        br.close();

        fileName = "./BOMutf-16.txt";
        log("Read: " + fileName);
        br = new BomFileReader(fileName);
        log(br.getEncoding() + "/" + br.getEncoder());
        char[] cbuf16 = new char[128];
        Arrays.fill(cbuf16, (char) 0);
        br.read(cbuf16, 0, 128);
        log(new String(cbuf));
        br.close();
        log(Arrays.equals(cbuf, cbuf16) ? "OK\r\n" : "ERROR\r\n");

        cbuf = (new String(cbuf)).toCharArray();
        fileName = "./temp.txt";
        log("Write/Read: " + fileName);
        log(Arrays.toString(BomTable.listEncoders()));
        for (String encoder : BomTable.listEncoders()) {
            BomFileWriter bw = new BomFileWriter(fileName, encoder);
            log("Write: " + bw.getEncoding() + "/" + bw.getEncoder());
            bw.write(cbuf);
            bw.flush();
            bw.close();
            br = new BomFileReader(fileName);
            log("Read: " + br.getEncoding() + "/" + br.getEncoder());
            br.read(cbuf);
            log(new String(cbuf));
            br.close();
            log(Arrays.equals(cbuf, cbuf16) ? "OK\r\n" : "FAIL!\r\n");
        }

        (new File(fileName)).delete();
    }
}
