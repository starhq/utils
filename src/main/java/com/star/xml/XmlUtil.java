package com.star.xml;

import com.star.collection.list.ListUtil;
import com.star.exception.ToolException;
import com.star.io.CharsetUtil;
import com.star.io.IoUtil;
import com.star.string.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * xml工具类
 *
 * @author starhq
 */
public final class XmlUtil {

    /**
     * 在XML中无效的字符 正则
     */
    public final static String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";

    private XmlUtil() {
    }

    /**
     * 读取解析XML文件
     */
    public static Document readXML(final Path path) {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(path.toFile());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ToolException(StringUtil.format("read xml failure,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 将String类型的XML转换为XML文档
     */
    public static Document parseXml(final String xmlStr) {
        final String xml = cleanInvalid(xmlStr);
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ToolException(StringUtil.format("parse xml string failure,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 将XML文档转换为String
     */
    public static String toStr(final Document doc, final String charset) {
        final StringWriter writer = new StringWriter();
        try {
            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, charset);
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (IllegalArgumentException | TransformerFactoryConfigurationError | TransformerException e) {
            throw new ToolException(
                    StringUtil.format("xml document to string failure,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 将XML文档写入到文件
     */
    public static void toFile(final Document doc, final Path path, final String charset) {
        String tmp = charset;
        if (StringUtil.isBlank(tmp)) {
            tmp = doc.getXmlEncoding();
        }
        if (StringUtil.isBlank(tmp)) {
            tmp = CharsetUtil.UTF_8;
        }

        BufferedWriter writer = null;
        try {
            writer = IoUtil.getWriter(Files.newOutputStream(path), CharsetUtil.charset(tmp));
            final Source source = new DOMSource(doc);
            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, charset);
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, new StreamResult(writer));
        } catch (IOException | TransformerFactoryConfigurationError | TransformerException e) {
            throw new ToolException(
                    StringUtil.format("xml persistence to file failure,the reason is: {}", e.getMessage()), e);
        } finally {
            IoUtil.close(writer);
        }
    }

    /**
     * 创建XML文档,添加根节点
     */
    public static Document createXml(final String rootElementName) {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ToolException(StringUtil
                    .format("create xml document and add root element failure,the reason is: {}", e.getMessage()), e);
        }
        final Document doc = builder.newDocument();
        doc.appendChild(doc.createElement(rootElementName));

        return doc;
    }

    /**
     * 根据节点名获得子节点列表
     */
    public static List<Element> getElements(final Element element, final String tagName) {
        final NodeList nodeList = element.getElementsByTagName(tagName);
        return transElements(element, nodeList);
    }

    /**
     * 根据节点名获得第一个子节点
     */
    public static Element getElement(final Element element, final String tagName) {
        final NodeList nodeList = element.getElementsByTagName(tagName);
        Element result = null;
        if (nodeList != null && nodeList.getLength() > 0) {
            final int length = nodeList.getLength();
            for (int i = 0; i < length; i++) {
                final Element childEle = (Element) nodeList.item(i);
                if (childEle == null || childEle.getParentNode() == element) {
                    result = childEle;
                }
            }
        }
        return result;
    }

    /**
     * 获得节点的内容
     */
    public static String elementText(final Element element, final String tagName) {
        final Element child = getElement(element, tagName);
        return child == null ? null : child.getTextContent();
    }

    /**
     * 获得节点的内容
     */
    public static String elementText(final Element element, final String tagName, final String defaultValue) {
        final Element child = getElement(element, tagName);
        return child == null ? defaultValue : child.getTextContent();
    }

    /**
     * 将NodeList转换为Element列表
     */
    public static List<Element> transElements(final NodeList nodeList) {
        return transElements(null, nodeList);
    }

    /**
     * 将NodeList转换为Element列表
     */
    public static List<Element> transElements(final Element parentEle, final NodeList nodeList) {
        final int length = nodeList.getLength();
        final List<Element> elements = ListUtil.newArrayList(length);
        for (int i = 0; i < length; i++) {
            final Element element = (Element) nodeList.item(i);
            if (parentEle == null || element.getParentNode() == parentEle) {
                elements.add(element);
            }
        }
        return elements;
    }

    /**
     * 去除xml无效字符
     */
    public static String cleanInvalid(final String xmlContent) {
        return StringUtil.isBlank(xmlContent) ? "" : xmlContent.replaceAll(INVALID_REGEX, "");
    }
}
