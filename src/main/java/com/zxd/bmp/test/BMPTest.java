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
        readFile("D:/2.bmp");
    }

//  1.BMP文件头(14字节)
//    BMP文件头数据结构含有 BMP 文件的类型、文件大小和位图起始位置等信息。 其结构定义如下: 　　
//    Int bfType; // 位图文件的类型，必须为 ' B '' M '两个字母 (0-1字节 ) 　
//    Int bfSize; // 位图文件的大小，以字节为单位 (2-5 字节 ) 　
//    usignedshort bfReserved1; // 位图文件保留字，必须为 0(6-7 字节 ) 　　
//    usignedshort bfReserved2; // 位图文件保留字，必须为 0(8-9 字节 ) 　
//    Int bfOffBits; // 位图数据的起始位置，以相对于位图 (10-13 字节 )
//    Int bfOffBits ; // 文件头的偏移量表示，以字节为单位
//  2.位图信息头(40 字节 ) 　
//    BMP 位图信息头数据用于说明位图的尺寸等信息。　
//    Int Size ; // 本结构所占用字节数 (14-17 字节 ) 　
//    Int image_width ; // 位图的宽度，以像素为单位 (18-21 字节 ) 　　
//    int image_heigh ; // 位图的高度，以像素为单位 (22-25 字节 ) 　
//    Int Planes; // 目标设备的级别，必须为 1(26-27 字节 ) 　　
//    int n biBitCount;// 每个像素所需的位数，必须是 1( 双色 ),(28-29 字节 ) 　　 // 4(16色),8(256色)或24(真彩色)之一　　
//    Int biCompression; // 位图压缩类型，必须是 0( 不压缩 ),(30-33 字节 ) 　　 // 1(BI_RLE8压缩类型)或2(BI_RLE4压缩类型)之一　　
//    Int n SizeImage; // 位图的大小，以字节为单位 (34-37 字节 ) 　　
//    Int biXPelsPerMeter; // 位图水平分辨率，每米像素数 (38-41 字节 ) 　　
//    Int biYPelsPerMeter; // 位图垂直分辨率，每米像素数 (42-45 字节 ) 　　
//    Int biClrUsed;// 位图实际使用的颜色表中的颜色数 (46-49 字节 ) 　　
//    Int biClrImportant;// 位图显示过程中重要的颜色数 (50-53 字节 )
    public static int[][] readFile(String path) {
        try {
            //创建读取文件的字节流
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            int headerInfoLength = 14;
            //1.读取14字节BMP文件头
            byte[] headerInfoArrays = new byte[headerInfoLength];
            bis.read(headerInfoArrays,0,headerInfoLength);
            //2.位图信息头
            int imageInfoHeaderLength = 40;
            byte[] infoHeaderArrays = new byte[imageInfoHeaderLength];
            bis.read(infoHeaderArrays,0,imageInfoHeaderLength);

            // 得到图片的高度和宽度
            int width = changeByteToInt(infoHeaderArrays,7);
            int height = changeByteToInt(infoHeaderArrays,11);
            System.out.println("width:"+width+",height:"+height);
            // 使用数组保存得图片的高度和宽度
            int[][] data = new int[height][width];
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

    //转成int
    public static int changeByteToInt(byte[] bi,int start){
        return (((int)bi[start]&0xff)<<24) | (((int)bi[start-1]&0xff)<<16) | (((int)bi[start-2]&0xff)<<8) | (int)bi[start-3]&0xff;
    }
}
