package com.star.captcha;

import com.star.common.RandomUtil;
import com.star.exception.IORuntimeException;
import com.star.image.ImageUtil;
import com.star.io.file.PathUtil;
import com.star.security.base64.Base64Util;
import com.star.string.StringUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

/**
 * 抽象验证码<br>
 * 抽象验证码实现了验证码字符串的生成、验证，验证码图片的写出<br>
 * 实现类通过实现{@link #createImage(String)} 方法生成图片对象
 *
 * @author looly
 */
public abstract class AbstractCaptcha implements ICaptcha {
    private static final long serialVersionUID = 3180820918087507254L;

    /**
     * 图片的宽度。
     */
    protected int width = 100;
    /**
     * 图片的高度。
     */
    protected int height = 37;
    /**
     * 验证码字符个数
     */
    protected int codeCount = 4;
    /**
     * 验证码干扰元素个数
     */
    protected int interfereCount = 15;
    /**
     * 字体
     */
    protected Font font;
    /**
     * 验证码
     */
    protected String code;
    /**
     * 验证码图片Buffer
     */
    protected BufferedImage image;

    /**
     * 构造
     *
     * @param width          图片宽
     * @param height         图片高
     * @param codeCount      字符个数
     * @param interfereCount 验证码干扰元素个数
     */
    public AbstractCaptcha(int width, int height, int codeCount, int interfereCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.interfereCount = interfereCount;
        // 字体高度设为验证码高度-2，留边距
        this.font = new Font("Courier", Font.PLAIN, this.height - 2);
        createCode();
    }

    @Override
    public void createCode() {
        generateCode();
        createImage(this.code);
    }

    /**
     * 生成验证码字符串
     *
     * @since 3.3.0
     */
    protected void generateCode() {
        this.code = RandomUtil.randomLetters(this.interfereCount);
    }

    /**
     * 根据生成的code创建验证码图片
     *
     * @param code 验证码
     */
    protected abstract void createImage(String code);

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public boolean verify(String userInputCode) {
        if (!StringUtil.isBlank(userInputCode)) {
            return getCode().equalsIgnoreCase(userInputCode);
        }
        return false;
    }

    /**
     * 验证码写出到文件
     *
     * @param path 文件路径
     * @throws IORuntimeException IO异常
     */
    public void write(String path) throws IORuntimeException {
        this.write(PathUtil.touch(Paths.get(path)).toFile());
    }

    /**
     * 验证码写出到文件
     *
     * @param file 文件
     * @throws IORuntimeException IO异常
     */
    public void write(File file) throws IORuntimeException {
        try (OutputStream out = PathUtil.getOutputStream(file.toPath(), false)) {
            this.write(out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override
    public void write(OutputStream out) {
        ImageUtil.write(this.getImage(), ImageUtil.IMAGE_TYPE_PNG, out);
    }

    /**
     * 获取验证码图
     *
     * @return 验证码图
     */
    public BufferedImage getImage() {
        if (null == this.image) {
            createCode();
        }
        return image;
    }

    /**
     * 获得图片的Base64形式
     *
     * @return 图片的Base64
     * @since 3.3.0
     */
    public String getImageBase64() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        write(byteArrayOutputStream);
        return Base64Util.encode(byteArrayOutputStream.toByteArray());
    }

    /**
     * 自定义字体
     *
     * @param font 字体
     */
    public void setFont(Font font) {
        this.font = font;
    }
}
