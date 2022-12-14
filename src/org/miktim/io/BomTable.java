/*
 * BOM table. MIT (c) 2022 miktim@mail.ru
 */
package org.miktim.io;

import java.util.Comparator;
import java.util.TreeMap;

public class BomTable {

    static TreeMap<String, byte[]> encoderMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    static TreeMap<byte[], String> prefixMap = new TreeMap<byte[], String>(new ByteArrayComparator());

    public static void addBom(String charsetName, byte[] bomPrefix) {
        encoderMap.put(charsetName.toUpperCase(), bomPrefix);
        if(bomPrefix.length >0)
        prefixMap.put(bomPrefix, charsetName.toUpperCase());
    }

    public static String[] listEncoders() {
        return (String[]) BomTable.encoderMap.keySet().toArray(new String[0]);
    }

    private static class ByteArrayComparator implements Comparator<byte[]> {

        @Override
        public int compare(byte[] left, byte[] right) {
            for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                int a = (left[i] & 0xff);
                int b = (right[j] & 0xff);
                if (a != b) {
                    return a - b;
                }
            }
            return left.length - right.length;
        }
    }

    static {
        addBom("UTF-8", new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        addBom("UTF-16", new byte[]{}); //Java UTF-16 output stream writer is the UTF-16BE BOM writer
        addBom("UTF-16BE", new byte[]{(byte) 0xFE, (byte) 0xFF});
        addBom("UTF-16LE", new byte[]{(byte) 0xFF, (byte) 0xFE});
        addBom("UTF-32",   new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF}); //BE
        addBom("UTF-32BE", new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF});
        addBom("UTF-32LE", new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        addBom("GB18030", new byte[]{(byte) 0x84, (byte) 0x31, (byte) 0x95, (byte) 0x33});
    }

}
