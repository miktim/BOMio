/*
 * BOM file writer. MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BomFileWriter extends BomOutputStreamWriter {

    public BomFileWriter(File file, String charsetName) throws IOException {
        super(new FileOutputStream(file), charsetName);
    }

    public BomFileWriter(String fileName, String charsetName) throws IOException {
        super(new FileOutputStream(fileName), charsetName);
    }

}
