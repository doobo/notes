package com._5fu8.admin.utils;

import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 * Created by doobo@foxmail.com on 2017/4/29.
 */
public abstract class AbstractNotesUtils {

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 计算指定页记录的分页总页数
     * @param pageSize
     * @param count
     * @return
     */
    public static int getPageCount(int pageSize, int count) {
        int pageCount = 1;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        return pageCount;
    }

    /**
     * MD5加密
     * @param str
     * @param key
     * @return
     */
    public static String getMD5(String str,String key) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            if(key != null && !key.isEmpty()){
                str = str + key;
            }
            mdInst.update(str.getBytes());
            byte[] md = mdInst.digest();
            StringBuilder sb = new StringBuilder(md.length * 2);
            for (byte b : md) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败！");
        }
    }

    /**
     * @return 形如 yyyyMMddHHmmssSSS-Z0000019558195832297
     * @return  的保证唯一的递增的序列号字符串
     */
    public synchronized static String getTimeMillisSequence(){
        long nanoTime = System.nanoTime();
        String preFix="";
        if (nanoTime<0){
            preFix="A";
            //负数补位A保证负数排在正数Z前面
            // ,解决正负临界值(如A9223372036854775807至Z0000000000000000000)问题
            nanoTime = nanoTime+Long.MAX_VALUE+1;
        }else{
            preFix="Z";
        }
        String nanoTimeStr = String.valueOf(nanoTime);

        int difBit=String.valueOf(Long.MAX_VALUE).length()-nanoTimeStr.length();
        for (int i=0;i<difBit;i++){
            preFix = preFix+"0";
        }
        nanoTimeStr = preFix+nanoTimeStr;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm"); //24小时制
        String timeMillisSequence=sdf.format(System.currentTimeMillis())+"-"+nanoTimeStr;
        return timeMillisSequence;
    }

}
