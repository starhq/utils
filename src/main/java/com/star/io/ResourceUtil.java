package com.star.io;


import com.star.clazz.ClassLoaderUtil;
import com.star.exception.ToolException;
import com.star.string.StringUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ResourceUtil {


    /**
     * 获取指定路径下的资源列表<br>
     * <p>
     * 路径格式必须为目录格式,用/分隔，例如:
     * <p>
     * <pre>
     *
     * config/a
     *
     * spring/xml
     *
     * </pre>
     *
     * @param resource 资源路径
     * @return 资源列表
     */
    public static List<URL> getResources(String resource) {
        final Enumeration<URL> resources;
        try {
            resources = ClassLoaderUtil.getClassLoader().getResources(resource);
        } catch (IOException e) {
            throw new ToolException(StringUtil.format("loading resource failure,the reason is {}", e.getMessage()), e);
        }
        List<URL> urlList = new ArrayList<>();
        while (resources.hasMoreElements()) {
            urlList.add(resources.nextElement());
        }
        return urlList;
    }

    /**
     * 获得资源相对路径对应的URL
     *
     * @param resource  资源相对路径
     * @param baseClass 基准Class，获得的相对路径相对于此Class所在路径，如果为{@code null}则相对ClassPath
     * @return {@link URL}
     */
    public static URL getResource(String resource, Class<?> baseClass) {
        return (null != baseClass) ? baseClass.getResource(resource) : ClassLoaderUtil.getClassLoader().getResource(resource);
    }
}
