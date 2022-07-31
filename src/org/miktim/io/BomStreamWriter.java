/*
 * BOM output stream writer. MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class BomStreamWriter extends Writer {

    private OutputStreamWriter osw;

    public BomStreamWriter(OutputStream out, String charsetName) throws UnsupportedEncodingException, IOException {
        super(out);
        byte[] prefix = BomTable.bomTable.get(charsetName);
        if (prefix == null) {
            throw new UnsupportedEncodingException();
        }
        out.write(prefix);
        osw = new OutputStreamWriter(out, charsetName);
    }

    public String getEncoding() {
        return osw.getEncoding();
    }

    @Override
    public void write(char[] chars, int i, int i1) throws IOException {
        osw.write(chars, i, i1);
    }

    @Override
    public void flush() throws IOException {
        osw.flush();
    }

    @Override
    public void close() throws IOException {
        osw.close();
    }
}
