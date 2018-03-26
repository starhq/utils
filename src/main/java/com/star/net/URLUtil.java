package com.star.net;

import com.star.exception.IORuntimeException;
import com.star.exception.ToolException;
import com.star.io.IoUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class URLUtil {

    public static InputStream getStream(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static BufferedReader getReader(URL url, Charset charset) {
        return IoUtil.getReader(getStream(url), charset);
    }

    public static URL getURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new ToolException("Error occured when get URL!");
        }
    }
}
