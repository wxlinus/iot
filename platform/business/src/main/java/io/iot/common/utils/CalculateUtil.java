package io.iot.common.utils;

/**
 * @author xupeng
 *
 */
public class CalculateUtil {

	//根据子网掩码和ip得到主机的广播地址
    public static String getBroadcastAddress(String ip, String subnetMask){
        String ipBinary = toBinary(ip);
        String subnetBinary = toBinary(subnetMask);
        String broadcastBinary = getBroadcastBinary(ipBinary, subnetBinary);
        String wholeBroadcastBinary=spiltBinary(broadcastBinary);
        return binaryToDecimal(wholeBroadcastBinary);
    }
 
    //二进制的ip字符串转十进制
    private static String binaryToDecimal(String wholeBroadcastBinary){
        String[] strings = wholeBroadcastBinary.split("\\.");
        StringBuilder sb = new StringBuilder(40);
        for (int j = 0; j < strings.length ; j++) {
            String s = Integer.valueOf(strings[j], 2).toString();
            sb.append(s).append(".");
        }
        return sb.toString().substring(0,sb.length()-1);
    }
 
    //按8位分割二进制字符串
    private static String spiltBinary(String broadcastBinary){
        StringBuilder stringBuilder = new StringBuilder(40);
        char[] chars = broadcastBinary.toCharArray();
        int count=0;
        for (int j = 0; j < chars.length; j++) {
            if (count==8){
                stringBuilder.append(".");
                count=0;
            }
            stringBuilder.append(chars[j]);
            count++;
        }
        return stringBuilder.toString();
    }
 
    //得到广播地址的二进制码
    private static String getBroadcastBinary(String ipBinary, String subnetBinary){
        int i = subnetBinary.lastIndexOf('1');
        String broadcastIPBinary = ipBinary.substring(0,i+1);
        for (int j = broadcastIPBinary.length(); j < 32 ; j++) {
            broadcastIPBinary=broadcastIPBinary+"1";
        }
        return broadcastIPBinary;
    }
 
    //转二进制
    private static String toBinary(String content){
        String binaryString="";
        String[] ipSplit = content.split("\\.");
        for ( String split : ipSplit ) {
            String s = Integer.toBinaryString(Integer.valueOf(split));
            int length = s.length();
            for (int i = length; i <8 ; i++) {
                s="0"+s;
            }
            binaryString = binaryString +s;
        }
        return binaryString;
    }
}
