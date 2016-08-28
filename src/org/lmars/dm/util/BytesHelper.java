/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lmars.dm.util;

/**
 *
 * @author Junia
 */
public class BytesHelper {
    
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[3] = (byte) (res & 0xff);// 最低位   
        targets[2] = (byte) ((res >> 8) & 0xff);// 次低位   
        targets[1] = (byte) ((res >> 16) & 0xff);// 次高位   
        targets[0] = (byte) (res >>> 24);// 最高位,无符号右移。   
        return targets;
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
    
}
