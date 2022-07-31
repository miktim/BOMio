/*
 * BOM input stream reader. MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class BomStreamReader extends Reader {

    private InputStreamReader isr;

    public BomStreamReader(InputStream in) throws IOException {
        super(in);
        byte[] prefix = new byte[2];
        if (in.read(prefix, 0, 2) != 2) {
            throw new IOException();
        }
        for (String key : BomTable.listEncoding()) {
            byte[] bomPrefix = BomTable.bomTable.get(key);
            while (prefix.length < bomPrefix.length
                    && Arrays.equals(prefix, Arrays.copyOf(bomPrefix, prefix.length))) {
                prefix = Arrays.copyOf(prefix, prefix.length + 1);
                if (in.read(prefix, prefix.length - 1, 1) != 1) {
                    throw new IOException();
                }
            }
            if (Arrays.equals(prefix, bomPrefix)) {
                isr = new InputStreamReader(in, key);
                return;
            }
        }
        throw new UnsupportedEncodingException();
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
