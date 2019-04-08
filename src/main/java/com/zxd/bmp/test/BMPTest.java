package com.zxd.bmp.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * @Project BMP-Test
 * @Package com.zxd.bmp.test
 * @Author：zouxiaodong
 * @Description:
 * @Date:Created in 17:58 2019/4/8.
 */
public class BMPTest {

    public static void main(String[] args){
        readFile("D:/1.bmp");
    }
    
    public static int[][] readFile(String path) {
        try {
            // 创建读取文件的字节流
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            // 读取时丢掉前面的18位，
            // 读取图片的18~21的宽度
            bis.skip(18);
            byte[] b = new byte[4];
            bis.read(b);
            // 读取图片的高度22~25
            byte[] b2 = new byte[4];
            bis.read(b2);

            // 得到图片的高度和宽度
            int width = byte2Int(b);
            int height = byte2Int(b2);
            System.out.println("width:"+width+",height:"+height);
            // 使用数组保存得图片的高度和宽度
            int[][] data = new int[height][width];
            int skipnum = 0;
            if (width * 3 / 4 != 0) {
                skipnum = 4 - width * 3 % 4;
            }
            // 读取位图中的数据，位图中数据时从54位开始的，在读取数据前要丢掉前面的数据
            bis.skip(28);
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    // bmp的图片在window里面世3个byte为一个像素
                    int blue = bis.read();
                    int green = bis.read();
                    int red = bis.read();
                    // 创建一个Color对象，将rgb作为参数传入其中
                    Color c = new Color(red, green, blue);
                    // Color c = new Color(blue,green,red);
                    // 将得到的像素保存到date数组中
                    data[i][j] = c.getRGB();
                }
                // 如果补0的个数不为0，则需要跳过这些补上的0
                if (skipnum != 0) {
                    bis.skip(skipnum);
                }
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
    // 将四个byte拼接成一个int
    public static int byte2Int(byte[] by) {
        int t1 = by[3] & 0xff;
        int t2 = by[2] & 0xff;
        int t3 = by[1] & 0xff;
        int t4 = by[0] & 0xff;
        int num = t1 << 24 | t2 << 16 | t3 << 8 | t4;
        return num;
    }
}
