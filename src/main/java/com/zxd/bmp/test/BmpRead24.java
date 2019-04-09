package com.zxd.bmp.test;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Project BMP-Test
 * @Package com.zxd.bmp.test
 * @Author：zouxiaodong
 * @Description:
 * @Date:Created in 17:58 2019/4/8.
 */
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
public class BmpRead24 extends JFrame{

    private static int width;

    private static int height;

    private static int[][] red,green,blue;

//    Graphics g;

    public static void main(String[] args){
//        readFile("D:/2.bmp");
        BmpRead24 bmp = new BmpRead24();
        bmp.init();
    }

    public void init(){
        try {
            // 通过bmp文件地址创建文件输入流对象
            FileInputStream fin = new FileInputStream("D:/2.bmp");
            // 根据文件输入流对象创建原始数据输入对象
            // 这里既可以用原始数据输入流来读取数据，也可以用缓冲输入流来读取，后者速度相比较快点。
//          java.io.DataInputStream bis = new java.io.DataInputStream(fin);
            BufferedInputStream bis = new BufferedInputStream(fin);
            // 建立两个字节数组来得到文件头和信息头的数据
            byte[] array1 = new byte[14];
            bis.read(array1, 0, 14);
            byte[] array2 = new byte[40];
            bis.read(array2, 0, 40);
            // 翻译bmp文件的数据，即将字节数据转化为int数据
            // 通过翻译得到位图数据的宽和高
            width = changeByteToInt(array2, 7);
            height = changeByteToInt(array2, 11);
            System.out.println("width:"+width+",height:"+height);
            // 调用可以将整个位图数据读取成byte数组的方法
            getInf(bis);
            bis.close();
            fin.close();
            // 创建BMP对象来显示图画
            showUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到位图数据的int数组
     *
     * @param bis
     *  数据输入流对象
     */
    public void getInf(java.io.BufferedInputStream bis) {
        // 给数组开辟空间
        red = new int[height][width];
        green = new int[height][width];
        blue = new int[height][width];
        // 通过计算得到每行计算机需要填充的字符数。
        // 为什么要填充？这是因为windows系统在扫描数据的时候，每行都是按照4个字节的倍数来读取的。
        // 因为图片是由每个像素点组成。而每个像素点都是由3个颜色分量来构成的，而每个分量占据1个字节。
        // 因此在内存存储中实际图片数据每行的长度是width*3。
        int skip_width = 0;
        int m = width * 3 % 4;
        if (m != 0) {
            skip_width = 4 - m;
        }
        // 通过遍历给数组填值
        // 这里需要注意，因为根据bmp的保存格式。
        // 位图数据中height的值如果是正数的话:
        // 那么数据就是按从下到上，从左到右的顺序来保存。这个称之为倒向位图。
        // 反之就是按从上到下，从左到右的顺序来保存。这个则称之为正向位图。
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                try {
                    // 这里遍历的时候，一定要注意本来像素是有RGB来表示，
                    // 但是在存储的时候由于windows是小段存储，所以在内存中是BGR顺序。
                    blue[i][j] = bis.read();
                    green[i][j] = bis.read();
                    red[i][j] = bis.read();
//                    System.out.println("i:"+i+",j:"+j+"red:"+red[i][j]+"green:"+green[i][j]+"blue:"+blue[i][j]);
                    // 这里一定要知道，其实系统在给位图数据中添加填充0的时候，都是加在每行的最后。
                    // 但是我们在使用dis.skipBytes（）这个方法的时候，却不一定要在最后一列。
                    // 系统在填充数据的时候，在数据上加了标记。
                    // 所以dis.skipBytes（）这个方法只要调用了，那么系统就会自动不读取填充数据。
                    if (j == 0) {
                        bis.skip(skip_width);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void showUI() {
        // 对窗体的属性进行设置
        this.setTitle("BMP解析");//设置标题
        this.setSize(width * 2, height * 2);//设置窗体大小
        this.setDefaultCloseOperation(3);//点击关闭，程序自动退出。
        this.setResizable(true);//设置窗体大小不可以调节
//        this.setLocationRelativeTo(null);//设置窗体出现在屏幕中间

        //创建自己的panel，用其来显示图形。
        //因为如果将图片设置到窗体上显示时，因为jframe是一个复合组件，上面的组件有多个paint方法，所以在paint的时候会画两次，
        //而panel是只需画一次。
        MyPanel panel = new MyPanel();
        Dimension di = new Dimension(width, height);//设置panel大小
        panel.setPreferredSize(di);
        this.add(panel);//窗体添加panel
        this.setVisible(true);//使窗体可见
    }

    class MyPanel extends JPanel{
        /**
         * 重写paint方法
         */
        public void paint(Graphics g) {
            // 这句话可写可不写，因为这句话是用来画jframe的contentPane的。
            // 而这里我们已经在下面定义了contentPane的方法了
            super.paint(g);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    g.setColor(new Color(red[i][j], green[i][j], blue[i][j]));
                    // 如果这里画点的话，是不能使用下面注释掉的方法的，不行的话，亲，自己试试吧
                    // 因为系统在画椭圆的时候，是先画出椭圆的外切矩形。而矩形的边框刚好是占据一个像素点。
                    // 因此也就出现了，jdk api中说g.drawOval的像素点是width+1,height+1。
                    // 如果亲，你有更好的理解，请告诉我们。欢迎交流！！！
                    // g.fillOval(j, i, 1, 1);
                    g.fillRect(j, i, 1, 1);// 这里可以使用画点的任何方法，除了上面那种特例。
                }
            }
        }
    }


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
            int bitCount = (((int)infoHeaderArrays[15]&0xff)<<8) | (int)infoHeaderArrays[14]&0xff;
            System.out.println("位数:"+bitCount);
            int imageSize = changeByteToInt(infoHeaderArrays,23);
            System.out.println("源图大小:"+imageSize);
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
