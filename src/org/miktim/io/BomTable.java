/*
 * BOM table. MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.io;

import java.util.HashMap;

public class BomTable {

    static HashMap<String, byte[]> bomTable = new HashMap<String, byte[]>();

    static {
        bomTable.put("UTF-8", new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        bomTable.put("UTF-16BE", new byte[]{(byte) 0xFE, (byte) 0xFF});
        bomTable.put("UTF-16LE", new byte[]{(byte) 0xFF, (byte) 0xFE});
    }

    public static void addBom(String charsetName, byte[] bomPrefix) {
        bomTable.put(charsetName, bomPrefix);
    }
    
    public static String[] listEncoding() {
        return (String[])BomTable.bomTable.keySet().toArray(new String[0]);
    }
}
