/*
 * BOM input stream reader. MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BomInputStreamReader extends Reader {

    private InputStreamReader isr;
    private String encoder = null;

    public BomInputStreamReader(InputStream in) throws IOException {
        super(in);
        BufferedInputStream bin = new BufferedInputStream(in);

        int b = 0;
        byte[] prefix = new byte[0];
        List<byte[]> prefixList = new ArrayList<>(BomTable.prefixMap.keySet());
        for (byte[] bomPrefix : prefixList) {
            while (prefix.length < bomPrefix.length
                    && Arrays.equals(prefix, Arrays.copyOf(bomPrefix, prefix.length))) {
                bin.mark(1);
                b = bin.read();
                if (b == -1) { // eof?
                    break;
                }
                prefix = Arrays.copyOf(prefix, prefix.length + 1);
                prefix[prefix.length - 1] = (byte) b;
            }
            if (b == -1 && prefix.length == 0) { // 
// behavior of Java BOM readers in case of "empty" stream             
                isr = new InputStreamReader(bin, "x-UTF-16LE-BOM");
                return;
            }
            if (Arrays.equals(prefix, bomPrefix)) {
                encoder = BomTable.prefixMap.get(bomPrefix);
            }
        }
        if (encoder == null) {
            throw new UnsupportedEncodingException();
        } else if (prefix.length != BomTable.encoderMap.get(encoder).length) {
            bin.reset();
        }
        isr = new InputStreamReader(bin, encoder);

    }

    public String getEncoder() {
        return encoder;
    }

    public String getEncoding() {
        return isr.getEncoding();
    }

    @Override
    public int read(char[] chars, int offset, int length) throws IOException {
        return isr.read(chars, offset, length);
    }

    @Override
    public void close() throws IOException {
        isr.close();
    }
}
