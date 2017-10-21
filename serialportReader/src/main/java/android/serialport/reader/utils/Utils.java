package android.serialport.reader.utils;

import android.serialport.reader.Application;
import android.util.DisplayMetrics;

/**
 * Created by ning on 17/9/1.
 */

public class Utils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dp) {
        DisplayMetrics metrics = Application.getGlobalContext().getResources().getDisplayMetrics();
        return (int) (metrics.density * dp + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        DisplayMetrics metrics = Application.getGlobalContext().getResources().getDisplayMetrics();
        return (int) (pxValue / metrics.density + 0.5f);
    }

    /**
     在Java中:
     char 16位 范围是2负的2的15次方到2的15次方的整数
     byte 虽然是8位，但是取值范围是负的2的7次方到2的7次方的整数
     在Java中，不存在Unsigned无符号数据类型，但可以轻而易举的完成Unsigned转换。

     方案一：如果在Java中进行流(Stream)数据处理，可以用DataInputStream类对Stream中的数据以Unsigned读取。
     Java在这方面提供了支持，可以用java.io.DataInputStream 类对象来完成对流内数据的Unsigned读取，该类提供了如下方法：
     （1）int readUnsignedByte () //从流中读取一个0~255(0xFF)的单字节数据，并以int数据类型的数据返回。返回的数据相当于C/C++语言中所谓的“BYTE”。
     （2）int readUnsignedShort () //从流中读取一个0~65535(0xFFFF)的双字节数据，并以int数据类型的数据返回。返回的数据相当于C/C++语言中所谓的“WORD”， 并且是以“低地址低字节”的方式返回的，所以程序员不需要额外的转换。
     方案二：利用Java位运算符，完成Unsigned转换。
     正常情况下，Java提供的数据类型是有符号signed类型的，可以通过位运算的方式得到它们相对应的无符号值，参见几个方法中的代码：
     public int getUnsignedByte (byte data){ //将data字节型数据转换为0~255 (0xFF 即BYTE)。
     return data&0x0FF ;
     }
     public int getUnsignedByte (short data){ //将data字节型数据转换为0~65535 (0xFFFF 即 WORD)。
     return data&0x0FFFF ;
     }
     public long getUnsignedIntt (int data){ //将int数据转换为0~4294967295 (0xFFFFFFFF即DWORD)。
     return data&0x0FFFFFFFF ;
     }
     */

    /**
     * 将data字节型数据转换为0~255 (0xFF 即BYTE)。
     * @param data data
     * @return int
     */
    public static int getUnsignedByte(byte data) {
        return data & 0x0FF;
    }

    //byte 数组与 int 的相互转换
    public static int byteArrayToInt(byte[] b) {
        return   b[0] & 0xFF |
                (b[1] & 0xFF) << 8 |
                (b[2] & 0xFF) << 16 |
                (b[3] & 0xFF) << 24;
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        return   b[0 + offset] & 0xFF |
                (b[1 + offset] & 0xFF) << 8 |
                (b[2 + offset] & 0xFF) << 16 |
                (b[3 + offset] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * 通过byte数组取到short
     *
     * @param b byte
     * @param offset  第几位开始取
     * @return short
     */
    public static short byteArrayToShort(byte[] b, int offset) {
        return (short) (((b[offset + 1] << 8) | b[offset + 0] & 0xff));
    }
}
