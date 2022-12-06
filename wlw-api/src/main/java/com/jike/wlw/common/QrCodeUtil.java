package com.jike.wlw.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/8/25- zhengzhoudong - 创建。
 */

public class QrCodeUtil {

    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;

    private static int qrCodeImageWidth = 200;

    private static int qrCodeImageHeight = 200;

    public static void main(String[] args) {

    }

//	public static void generateQRCode(String content,OutputStream out) throws WriterException, IOException {
//        String filePostfix="png";
//        encode(content,filePostfix, qrCodeImageWidth, qrCodeImageHeight,out);
//	}

    /**
     * 生成QRCode二维码<br>
     * 在编码时需要将com.google.zxing.qrcode.encoder.Encoder.java中的<br>
     * static final String DEFAULT_BYTE_MODE_ENCODING = "ISO8859-1";<br>
     * 修改为UTF-8，否则中文编译后解析不了<br>
     *
     * @param contents    二维码的内容
     * @param format      qrcode码的生成格式
     * @param width       图片宽度
     * @param height      图片高度
     * @throws WriterException
     * @throws IOException
     */
    public static void generateQRCode(String contents, String format, int width, int height, OutputStream out)
            throws WriterException, IOException {
        contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height);
        writeToBytes(bitMatrix, format,out);
    }

    /**
     * 生成二维码图片<br>
     *
     * @param matrix
     * @param format 图片格式
     * @throws IOException
     */
    private static void writeToBytes(BitMatrix matrix, String format, OutputStream out) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        ImageIO.write(image, format, out);

    }

    /**
     * 生成二维码内容<br>
     *
     * @param matrix
     * @return
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) == true ? BLACK : WHITE);
            }
        }
        return image;
    }

    // inputStream转outputStream
    public static ByteArrayOutputStream parse(final InputStream in) throws Exception {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }


    // outputStream转inputStream
    public static ByteArrayInputStream parse(final OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    // 图片生成，文字换行
    public static void fontLineFeed(Graphics g, String strContent, int x, int y, int rowWidth, int maxRow) {
        //获取字符串的总宽度
        char[] strcha = strContent.toCharArray();
        int strWidth = g.getFontMetrics().charsWidth(strcha, 0, strContent.length());

        //获取字符高度
        int strHeight = g.getFontMetrics().getHeight();

        // 设置换行
        if (strWidth > rowWidth) {
            //每一行字符的个数
            int rowstrnum = 0;
            System.out.println(strContent.length());
            rowstrnum = (rowWidth * strContent.length()) / strWidth;

            //字符行数
            int rows = 0;
            if (strWidth % rowWidth > 0) {
                rows = strWidth / rowWidth + 1;
            } else {
                rows = strWidth / rowWidth;
            }

            for (int i = 0; i < rows; i++) {
                String temp = "";
                // 当前行数等于限制最大行数时
                if (i >= maxRow - 1) {
                    if (strContent.length() - i * rowstrnum > rowstrnum) {
                        temp = strContent.substring(i * rowstrnum, i * rowstrnum + (rowstrnum - 3)) + "...";
                    } else {
                        temp = strContent.substring(i * rowstrnum, strContent.length());
                    }

                    if (i > 0) {
                        y = y + strHeight;
                    }
                    g.drawString(temp, x, y);

                    break;
                }

                //获取各行的String
                if (i == rows - 1) {
                    //最后一行
                    temp = strContent.substring(i * rowstrnum, strContent.length());
                } else {
                    temp = strContent.substring(i * rowstrnum, i * rowstrnum + rowstrnum);
                }
                if (i > 0) {
                    //第一行不需要增加字符高度，以后的每一行在换行的时候都需要增加字符高度
                    y = y + strHeight;
                }
                g.drawString(temp, x, y);
            }
        } else {
            // 只有一行时直接绘制
            g.drawString(strContent, x, y);
        }
    }

}
