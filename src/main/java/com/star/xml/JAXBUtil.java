package com.star.xml;

import com.star.exception.ToolException;
import com.star.string.StringUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * xml和对象互转,用的是jaxb
 *
 * @author starhq
 */
public final class JAXBUtil {

    /**
     * 缓存class和对象的jaxbcontext
     */
    private static final ClassValue<JAXBContext> PROXY = new ClassValue<JAXBContext>() {

        /**
         * 根据转进来的对象封装jaxbcontext
         */
        @Override
        protected JAXBContext computeValue(final Class<?> type) {
            JAXBContext jaxbContext;
            try {
                jaxbContext = JAXBContext.newInstance(new Class<?>[]{type}, new HashMap<String, String>());
            } catch (JAXBException e) {
                throw new ToolException(
                        StringUtil.format("create jaxbcontext failure,the reason is: {}", e.getMessage()), e);
            }
            return jaxbContext;
        }
    };

    private JAXBUtil() {
    }

    /**
     * 对象转xml（单个对象）
     *
     * @param clazzz   类
     * @param instatce 对象
     * @param format   是否格式化输出
     * @param <T>      范型
     * @return xml字符串
     */
    public static <T> String object2Xml(final Class<T> clazzz, final T instatce, final boolean format) {
        final JAXBContext jaxbContext = PROXY.get(clazzz);
        final StringWriter stringWriter = new StringWriter();
        try {
            final Marshaller marshaller = jaxbContext.createMarshaller();
            if (format) {
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            }
            marshaller.marshal(instatce, stringWriter);
        } catch (JAXBException e) {
            throw new ToolException(StringUtil.format("object to xml failure,the reason is: {}", e.getMessage()), e);
        }
        return stringWriter.toString();
    }

    /**
     * xml转对象
     *
     * @param clazzz 类
     * @param xml    字符串
     * @param <T>    范型
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2Object(final Class<T> clazzz, final String xml) {
        final JAXBContext jaxbContext = PROXY.get(clazzz);
        final StringReader stringReader = new StringReader(xml);
        try {
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(stringReader);
        } catch (JAXBException e) {
            throw new ToolException(StringUtil.format("xml to object's failure the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 对象转xml持久化到文件
     *
     * @param clazz    类
     * @param instance 对象
     * @param file     文件
     * @param format   格式化输出
     * @param <T>      范型
     */
    public static <T> void object2File(final Class<T> clazz, final T instance, final File file, final boolean format) {
        final JAXBContext jaxbContext = PROXY.get(clazz);
        try {
            final Marshaller marshaller = jaxbContext.createMarshaller();
            if (format) {
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            }
            marshaller.marshal(instance, file);
        } catch (JAXBException e) {
            throw new ToolException(StringUtil.format(
                    "object conver to xml,then persistence to file failure,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 文件中xml还原成对象
     *
     * @param clazz 类
     * @param file  文件
     * @param <T>   范型
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T fileToObject(final Class<T> clazz, final File file) {
        final JAXBContext jaxbContext = PROXY.get(clazz);
        try {
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new ToolException(StringUtil.format("read xml from file then restore object failue,the reason is: {}",
                    e.getMessage()), e);
        }
    }

    /**
     * 输入流中的xml转成对象
     *
     * @param clazz       类
     * @param inputStream 输入流
     * @param <T>         范型
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T streamToObject(final Class<T> clazz, final InputStream inputStream) {
        final JAXBContext jaxbContext = PROXY.get(clazz);
        try {
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new ToolException(
                    StringUtil.format("input stream to object failure,the reason is: {}", e.getMessage()), e);
        }
    }
}
