package com.star.net;

import com.star.exception.IORuntimeException;
import com.star.regex.Validator;
import com.star.string.HexUtil;
import com.star.string.StringUtil;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 网络辅助工具类
 *
 * @author http://git.oschina.net/loolly/hutool
 */
public final class NetUtil {

    /**
     * 本地ip
     */
    public final static String LOCAL_IP = "127.0.0.1";

    private NetUtil() {
    }

    /**
     * long转ipv4
     *
     * @param longIP ip的long值
     * @return ipv4
     */
    public static String longToIpv4(final long longIP) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(longIP >>> 24)).append(StringUtil.C_DOT)
                .append(String.valueOf((longIP & 0x00FFFFFF) >>> 16)).append(StringUtil.C_DOT)
                .append(String.valueOf((longIP & 0x0000FFFF) >>> 8)).append(StringUtil.C_DOT)
                .append(String.valueOf(longIP & 0x000000FF));
        return builder.toString();
    }

    /**
     * ipv4转long
     *
     * @param strIP ip字符串
     * @return long值
     */
    public static long ipv4ToLong(final String strIP) {
        long result;
        if (strIP.matches(Validator.IPV4)) {
            long[] ipArray = new long[4];
            // 先找到IP地址字符串中.的位置
            final int position1 = strIP.indexOf('.');
            final int position2 = strIP.indexOf('.', position1 + 1);
            final int position3 = strIP.indexOf('.', position2 + 1);
            // 将每个.之间的字符串转换成整型
            ipArray[0] = Long.parseLong(strIP.substring(0, position1));
            ipArray[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
            ipArray[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
            ipArray[3] = Long.parseLong(strIP.substring(position3 + 1));
            result = (ipArray[0] << 24) + (ipArray[1] << 16) + (ipArray[2] << 8) + ipArray[3];
        } else {
            result = 0;
        }
        return result;
    }


    /**
     * 验证端口是否有效
     *
     * @param port 端口号
     * @return 是否可用
     */
    public static boolean isValidPort(final int port) {
        return port >= 0 && port <= 0xFFFF;
    }

    /**
     * 验证端口是否可用
     *
     * @param port 端口
     * @return 是否可用
     */
    public static boolean isUsableLocalPort(final int port) {
        boolean result;
        if (isValidPort(port)) {
            try {
                new Socket(LOCAL_IP, port).close();
                result = false;
            } catch (IOException e) {
                result = true;
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 是否内网地址
     *
     * @param ipAddress ip地址
     * @return 是否内网
     */
    public static boolean isInnerIP(final String ipAddress) {

        final long ipNum = ipv4ToLong(ipAddress);

        return isInner(ipNum, 167772160, 184549375) || isInner(ipNum, 2886729728L, 2887778303L)
                || isInner(ipNum, 3232235520L, 3232301055L) || 2130706433 == ipNum;
    }

    /**
     * 查询本机ipv4
     *
     * @return 本机ip地址
     */
    public static Set<String> localIpv4s() {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("Get network interface failure ,the reason is: {}", e.getMessage()), e);
        }

        final HashSet<String> ipSet = new HashSet<>();

        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    ipSet.add(inetAddress.getHostAddress());
                }
            }
        }
        return ipSet;
    }

    /**
     * ip的最后一部分用*来替代
     *
     * @param ipString ip
     * @return 格式化过的ip
     */
    public static String hideIpPart(final String ipString) {
        return new StringBuilder(ipString.length()).append(ipString.substring(0, ipString.lastIndexOf(StringUtil.C_DOT) + 1))
                .append('*').toString();
    }

    /**
     * 隐藏掉IP地址的最后一部分为 * 代替
     *
     * @param ip IP地址
     * @return 隐藏部分后的IP
     */
    public static String hideIpPart(final long ip) {
        return hideIpPart(longToIpv4(ip));
    }


    /**
     * 查询本机mac地址
     *
     * @return mac地址
     */
    public static String getMac() {
        NetworkInterface network;
        byte[] mac;
        try {
            network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            mac = network.getHardwareAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new IORuntimeException(StringUtil.format("get local mac address failue,the reason is: {}", e.getMessage()),
                    e);
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            stringBuilder.append(HexUtil.encode(new byte[]{mac[i]}));
            if (i < mac.length - 1) {
                stringBuilder.append('-');
            }
        }
        return stringBuilder.toString();
    }

    private static boolean isInner(final long userIp, final long begin, final long end) {
        return userIp >= begin && userIp <= end;
    }

}
