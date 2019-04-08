package com.zxd.bmp.test;

/**
 * @Project BMP-Test
 * @Package com.zxd.bmp.test
 * @Author：zouxiaodong
 * @Description:
 * @Date:Created in 18:05 2019/4/8.
 */
public class Color {

    private int red;

    private int green;

    private int blue;

    Color(int red,int green,int blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRGB(){
        return this.red + this.green + this.blue;
    }
}
