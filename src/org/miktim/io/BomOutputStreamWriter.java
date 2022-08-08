/*
 * BOM output stream writer. MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class BomOutputStreamWriter extends Writer {

    private OutputStreamWriter osw;
    private OutputStream os;
    private String encoder = null;
    private byte[] prefix;

    public BomOutputStreamWriter(OutputStream out, String charsetName) throws UnsupportedEncodingException, IOException {
        super(out);
        prefix = BomTable.encoderMap.get(charsetName);
        if (prefix == null) {
            throw new UnsupportedEncodingException();
        }
        os = out;
        encoder = charsetName.toUpperCase();
//        out.write(prefix);
        osw = new OutputStreamWriter(out, charsetName);
    }

    public String getEncoder() {
        return encoder;
    }

    public String getEncoding() {
        return osw.getEncoding();
    }

    @Override
    public void write(char[] chars, int offset, int length) throws IOException {
        if (prefix != null && length > 0) {
// behavior of Java BOM writers in case of "empty" streams        
            os.write(prefix);
            prefix = null;
        }
        osw.write(chars, offset, length);
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
