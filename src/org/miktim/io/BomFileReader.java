/*
 * BOM file reader. MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BomFileReader extends BomStreamReader {

    public BomFileReader(File file) throws IOException {
        super(new FileInputStream(file));
    }

    public BomFileReader(String name) throws IOException {
        super(new FileInputStream(name));
    }

}
