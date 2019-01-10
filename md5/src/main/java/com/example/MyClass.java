package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyClass {

    //apk文件所在目录
    static String fileDir = "/Users/username/Desktop/apkfile";

    public static void main(String[] args){
        File file = new File(fileDir);
        File[] fi = file.listFiles();
        for (int i = 0; i < fi.length; i++) {
            if (fi[i].isFile()) {
                try{
                    System.out.println("文件："+fi[i].getName() + "的MD5值为：" + getFileMD5(fi[i]));
                }catch (Exception e){

                }
            }
        }
    }

    /**
     * get file md5
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        in.close();
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

}
