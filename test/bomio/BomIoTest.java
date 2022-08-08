/*
 * BOM io test. MIT (c) 2022 miktim@mail.ru
 * JDK 8 and higher required!
 */
package bomio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import org.miktim.io.BomFileReader;
import org.miktim.io.BomFileWriter;
import org.miktim.io.BomTable;

public class BomIoTest {

    public static void log(String s) {
        System.out.println(s);
    }

    static File makeFile(String fileName) {
        return new File(new File(".").getAbsolutePath(), fileName);
    }

    public static void main(String[] args) throws IOException {
        log("BOM IO Test\r\n");

        String text = null;
        
        String j8BomWriters = "UTF-16,x-UTF-16LE-BOM,x-UTF-32BE-BOM,x-UTF-32LE-BOM";

        String j8BomReaders = "x-UTF-16LE-BOM,x-UTF-32BE-BOM,x-UTF-32LE-BOM";
        String bomWriters = "UTF-16LE,UTF-32BE,UTF-32LE";

        File file = makeFile("utf-8-bom.txt");
        log("Read file " + file.getName());
        BomFileReader brd = new BomFileReader(file);
        log("BOM Reader encoder/encoding: " + brd.getEncoder() + "/" + brd.getEncoding());
        char[] buf = new char[512];
        brd.read(buf);
        brd.close();
        text = new String(buf);
        log(text);

        file = makeFile("temp.txt");
        log("Test BOM Reader");
        log("Java8 Writer BOM encoders: " + j8BomWriters);
        log("BOM Reader encoders: " + Arrays.toString(BomTable.listEncoders()) + "\r\n");
        for (String encoder : j8BomWriters.split(",")) {
            OutputStreamWriter wrt = new OutputStreamWriter(
                    new FileOutputStream(file), encoder);
            log("Java Writer encoder/encoding: " + encoder + "/" + wrt.getEncoding());
            wrt.write(text.toCharArray());
            wrt.close();

            BomFileReader rdr = new BomFileReader(file);
            log("BOM Reader encoder/encoding: " + rdr.getEncoder() + "/" + rdr.getEncoding());
            char[] cbuf = new char[text.length()];
            rdr.read(cbuf);
            rdr.close();
            String s = new String(cbuf);
            log(s);
            log(s.equals(text) ? "OK\r\n" : "FAIL!\r\n");
        }

        log("Test BOM Writer with matching Java8 BOM encoders");
        log("BOM Writer encoders: " + bomWriters);
        log("Java8 Reader BOM encoders: " + j8BomReaders + "\r\n");
        String[] bWriters = bomWriters.split(",");
        String[] jReaders = j8BomReaders.split(",");
        for (int i = 0; i < bWriters.length; i++) {
            String encoder = bWriters[i];
            BomFileWriter wrt = new BomFileWriter(file, encoder);
            log("BOM Writer encoder/encoding: " + encoder + "/" + wrt.getEncoding());
            wrt.write(text.toCharArray());
            wrt.close();

            encoder = jReaders[i];
            InputStreamReader rdr = new InputStreamReader(
                    new FileInputStream(file), encoder);
            log("Java Reader encoder/encoding: " + encoder + "/" + rdr.getEncoding());
            char[] cbuf = new char[text.length()];
            rdr.read(cbuf);
            rdr.close();
            String s = new String(cbuf);
            log(s);
            log(s.equals(text) ? "OK\r\n" : "FAIL!\r\n");
        }

        file.delete();
    }
}
