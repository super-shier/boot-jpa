package com.shier.common.boot.jpa.common.utils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class VerifyCodeUtil {


    public static String service(BufferedImage image, Graphics g) throws IOException {
        Graphics2D g2d = (Graphics2D) g;       //创建Grapchics2D对象
        Random random = new Random();
        Font mfont = new Font("宋体", Font.ROMAN_BASELINE, 25); //定义字体样式
        //g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());    //绘制背景
        g.setFont(mfont);                   //设置字体
        //g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        //绘制100条颜色和位置全部为随机产生的线条,该线条为2f
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(image.getWidth() - 1);
            int y = random.nextInt(image.getHeight() - 1);
            int x1 = random.nextInt(6) + 1;
            int y1 = random.nextInt(12) + 1;
            BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL); //定制线条样式
            Line2D line = new Line2D.Double(x, y, x + x1, y + y1);
            g2d.setStroke(bs);
            g2d.draw(line);     //绘制直线
        }
        //输出由英文，数字，和中文随机组成的验证文字，具体的组合方式根据生成随机数确定。
        String sRand = "";
        String ctmp = "";
        //制定输出的验证码为四位
        String[] codeSequence = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for (int i = 0; i < 4; i++) {
            ctmp = codeSequence[random.nextInt(codeSequence.length)];
            sRand += ctmp;
            Color color = new Color(20 + random.nextInt(110), 20 + random.nextInt(110), random.nextInt(110));
            g.setColor(color);
            //将生成的随机数进行随机缩放并旋转制定角度 PS.建议不要对文字进行缩放与旋转,因为这样图片可能不正常显示
            /*将文字旋转制定角度*/
            Graphics2D g2d_word = (Graphics2D) g;
            AffineTransform trans = new AffineTransform();
            trans.rotate((25) * 3.14 / 180, 15 * i + 8, 7);
            /*缩放文字*/
            float scaleSize = random.nextFloat() + 0.8f;
            if (scaleSize > 1f) scaleSize = 1f;
            trans.scale(scaleSize, scaleSize);
            g2d_word.setTransform(trans);
            g.drawString(ctmp, 15 * i + 18, 14);
        }
        return sRand;
    }
}
