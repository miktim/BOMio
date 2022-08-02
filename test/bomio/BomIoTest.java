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
        log("BOM IO Test");
        log(Arrays.toString(BomTable.listEncoders()));
        String fileName = "./BOMutf-8.txt";
        log("Read: " + fileName);
        BomFileReader br = new BomFileReader(fileName);
        log(br.getEncoding());
        char[] cbuf = new char[128];
        br.read(cbuf, 0, 128);
        log(new String(cbuf));
        br.close();
        fileName = "./BOMutf-16.txt";
        log("Read: " + fileName);
        br = new BomFileReader(fileName);
        log(br.getEncoding());
        Arrays.fill(cbuf, (char) 0);
        br.read(cbuf, 0, 128);
        log(new String(cbuf));
        br.close();
        cbuf = (new String(cbuf)).toCharArray();
        fileName = "./temp.txt";
        log("Write/Read: " + fileName);
        BomFileWriter bw = new BomFileWriter(fileName, "UTF-32LE");
        bw.write(cbuf);
        bw.flush();
        bw.close();
        br = new BomFileReader(fileName);
        log(br.getEncoding());
        br.read(cbuf);
        log(new String(cbuf));
        br.close();
        
        bw = new BomFileWriter(fileName, "Utf-32Be");
        bw.write(cbuf);
        bw.flush();
        bw.close();

        (new File(fileName)).delete();
    }
}
