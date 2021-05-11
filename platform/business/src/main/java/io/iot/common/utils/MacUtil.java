package io.iot.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;


public class MacUtil {
    
    public static String getLocalMacByIp(String ip) throws SocketException, IOException {
        NetworkInterface ne= NetworkInterface.getByInetAddress(InetAddress.getByName(ip));
        byte[]mac=ne.getHardwareAddress();
        String mac_s=hexByte(mac[0])+"-"+
        hexByte(mac[1])+"-"+ 
        hexByte(mac[2])+"-"+
        hexByte(mac[3])+"-"+ 
        hexByte(mac[4])+"-"+
        hexByte(mac[5])
        ;
        return mac_s;
    }
    
    private static String hexByte(byte b)
        {
            String s="000000"+ Integer.toHexString(b);
            return s.substring(s.length()-2);
        }
    
    /**
     * 获取当前操作系统名称. return 操作系统名称 例如:windows,Linux,Unix等.
     */
    public static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }

    /**
     * 获取Unix网卡的mac地址.
     * 
     * @return mac地址
     */
    public static String getUnixMACAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {
            /**
             * Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
             */
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                /**
                 * 寻找标示字符串[hwaddr]
                 */
                index = line.toLowerCase().indexOf("hwaddr");
                /**
                 * 找到了
                 */
                if (index != -1) {
                    /**
                     * 取出mac地址并去除2边空格
                     */
                    mac = line.substring(index + "hwaddr".length() + 1).trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.getMessage();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            bufferedReader = null;
            process = null;
        }
        return mac;
    }

    /**
     * 获取Linux网卡的mac地址.
     * 
     * @return mac地址
     */
    public static String getLinuxMACAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {
            /**
             * linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
             */
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                index = line.toLowerCase().indexOf("硬件地址");
                /**
                 * 找到了
                 */
                if (index != -1) {
                    /**
                     * 取出mac地址并去除2边空格
                     */
                    mac = line.substring(index + 4).trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.getMessage();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            bufferedReader = null;
            process = null;
        }

        return mac;
    }

    /**
     * 获取widnows网卡的mac地址.
     * 
     * @return mac地址
     */
    public static String getWindowsMACAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {
            /**
             * windows下的命令，显示信息中包含有mac地址信息
             */
            process = Runtime.getRuntime().exec("ipconfig /all");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), "GBK")); // windows系统都是GBK编码，不加GBK读出的中文是乱码
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                /**
                 * 寻找标示字符串[physical address]
                 */
                index = line.toLowerCase().indexOf("physical address");
                if (index == -1) {
                    /**
                     * 寻找标示字符串[物理地址]
                     */
                    index = line.toLowerCase().indexOf("物理地址");
                     
                }

                if (index != -1) {
                    index = line.indexOf(":");
                    if (index != -1) {
                        /**
                         * 取出mac地址并去除2边空格
                         */
                        mac = line.substring(index + 1).trim();
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.getMessage();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            bufferedReader = null;
            process = null;
        }
        return mac;
    }

    /**
     * 获取widnows网卡的mac地址.
     * 
     * @return mac地址
     */
    public static String getWindowsMACAddress(String ip) {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {
            /**
             * windows下的命令，显示信息中包含有mac地址信息
             */
            // process = Runtime.getRuntime().exec("ipconfig /all");
            process = Runtime.getRuntime().exec("nbtstat -A " + ip);
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), "GBK")); // windows系统都是GBK编码，不加GBK读出的中文是乱码
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                /**
                 * 寻找标示字符串[physical address]
                 */
                 index = line.toLowerCase().indexOf("mac 地址 =");
                 
                if (index != -1) {
                    index = line.indexOf("=");
                    if (index != -1) {
                        /**
                         * 取出mac地址并去除2边空格
                         */
                        mac = line.substring(index + 1).trim();
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.getMessage();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            bufferedReader = null;
            process = null;
        }
        return mac;
    }

    /**
     * 获取客户端mac
     * 
     * @description {TODO}
     * @return
     */
    public static String getMACAddress(String ip) {
        String os = getOSName();
        String mac = "";
        if (os.startsWith("windows")) {
            mac = getWindowsMACAddress(ip);
        } else if (os.startsWith("linux")) {
            mac = getLinuxMACAddress();
        } else {
            mac = getUnixMACAddress();
        }
        return mac;
    }

    public static void main(String[] args) {
		String macAddress = MacUtil.getWindowsMACAddress("192.168.100.14");
		System.err.println(macAddress);
	}
       
}