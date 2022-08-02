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

public class BomStreamReader extends Reader {

    private InputStreamReader isr;

    public BomStreamReader(InputStream in) throws IOException {
        super(in);
        BufferedInputStream bin = new BufferedInputStream(in);
        byte[] prefix = new byte[2];
        if (bin.read(prefix, 0, prefix.length) != prefix.length) {
            throw new IOException();
        }
        String encoder = null;
        List<byte[]> prefixList = new ArrayList(BomTable.prefixMap.keySet());
        for (byte[] bomPrefix : prefixList) {
            while (prefix.length < bomPrefix.length
                    && Arrays.equals(prefix, Arrays.copyOf(bomPrefix, prefix.length))) {
                prefix = Arrays.copyOf(prefix, prefix.length + 1);
                bin.mark(1);
                if (bin.read(prefix, prefix.length - 1, 1) != 1) {
                    throw new IOException();
                }
            }
            if (Arrays.equals(prefix, bomPrefix)) {
                encoder = BomTable.prefixMap.get(bomPrefix);
            }
        }
        if (encoder == null) {
            throw new UnsupportedEncodingException();
        }
        if (prefix.length != BomTable.encoderMap.get(encoder).length) {
            bin.reset();
        }
        isr = new InputStreamReader(bin, encoder);

    }

    public String getEncoding() {
        return isr.getEncoding();
    }

    @Override
    public int read(char[] cbuf, int offset, int length) throws IOException {
        return isr.read(cbuf, offset, length);
    }

    @Override
    public void close() throws IOException {
        isr.close();
    }
}
