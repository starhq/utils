package com.star.captcha;

import com.star.common.RandomUtil;
import com.star.image.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 圆圈干扰验证码
 *
 * @author looly
 * @since 3.2.3
 */
public class CircleCaptcha extends AbstractCaptcha {
    private static final long serialVersionUID = -7096627300356535494L;

    /**
     * 构造
     *
     * @param width  图片宽
     * @param height 图片高
     */
    public CircleCaptcha(int width, int height) {
        this(width, height, 5);
    }

    /**
     * 构造
     *
     * @param width     图片宽
     * @param height    图片高
     * @param codeCount 字符个数
     */
    public CircleCaptcha(int width, int height, int codeCount) {
        this(width, height, codeCount, 15);
    }

    /**
     * 构造
     *
     * @param width          图片宽
     * @param height         图片高
     * @param codeCount      字符个数
     * @param interfereCount 验证码干扰元素个数
     */
    public CircleCaptcha(int width, int height, int codeCount, int interfereCount) {
        super(width, height, codeCount, interfereCount);
    }

    @Override
    public void createImage(String code) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = ImageUtil.createGraphics(this.image, Color.WHITE);

        // 画字符串
        g.setFont(font);
        int len = code.length();
        int w = width / len;
        AlphaComposite ac3;
        for (int i = 0; i < len; i++) {
            // 指定透明度
            ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f);
            g.setComposite(ac3);
            g.setColor(ImageUtil.randomColor());
            g.drawString(String.valueOf(code.charAt(i)), i * w, RandomUtil.randomInt(height >> 1) + (height >> 1));
        }

        // 随机画干扰圈圈
        drawInterfere(g);
    }

    // ----------------------------------------------------------------------------------------------------- Private method start

    /**
     * 画随机干扰
     *
     * @param g {@link Graphics2D}
     */
    private void drawInterfere(Graphics2D g) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < this.interfereCount; i++) {
            g.setColor(ImageUtil.randomColor(random));
            g.drawOval(random.nextInt(width), random.nextInt(height), random.nextInt(height >> 1), random.nextInt(height >> 1));
        }
    }
    // ----------------------------------------------------------------------------------------------------- Private method end
}
