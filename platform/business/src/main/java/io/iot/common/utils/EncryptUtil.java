package io.iot.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;

/**
 * @author xupeng
 * 加解密工具
 */
@Slf4j
public class EncryptUtil {

	private static final String ALGORITHM_DES = "DES/ECB/PKCS7Padding";
	
	private static final String ALGORITHM_AES = "AES/CBC/PKCS7Padding";
	//设置java支持PKCS7Padding
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	// 获取加密或解密的Cipher对象：负责完成加密或解密工作
	private static Cipher GetCipher(int opmode, String key) {
		try {
			// 根据传入的秘钥内容生成符合DES加密解密格式的秘钥内容
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			// 获取DES秘钥生成器对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 生成秘钥：key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			// 获取DES/ECB/PKCS7Padding该种级别的加解密对象
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			// 初始化加解密对象【opmode:确定是加密还是解密模式；secretKey是加密解密所用秘钥】
			cipher.init(opmode, secretKey);
			return cipher;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
     * DES算法，加密
     * 
     * @param data
     *            待加密字符串
     * @param key
     *            加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws InvalidAlgorithmParameterException
     * @throws Exception
     */
	public static String encode(String data, String key) {
		if (data == null || data.isEmpty())
			return null;
		try {
			// 获取加密对象【Cipher.ENCRYPT_MODE：指定加密模式为1】
			Cipher cipher = GetCipher(Cipher.ENCRYPT_MODE, key);
			if (cipher == null) {
				return null;
			} else {
				// 设置加密的字符串为utf-8模式并且加密，返回加密后的byte数组。
				byte[] byteHex = cipher.doFinal(data.getBytes("UTF-8"));
//				return byteToHexString(byteHex);// 对加密后的数组进制转换
				return Base64.encode(byteHex);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	/**
     * DES算法，解密
     * 
     * @param data
     *            待解密字符串
     * @param key
     *            解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception
     * @throws Exception
     *             异常
     */
	public static String decode(String data, String key) throws Exception {
		if (data == null || data.isEmpty())
			return null;
		try {
			//先把待解密的字符串转成Char数组类型，然后进行进制转换。
			byte[] b = HexUtil.decodeHex(data.toCharArray());
			//获取解密对象【Cipher.DECRYPT_MODE：指定解密模式为2】
			Cipher cipher = GetCipher(Cipher.DECRYPT_MODE, key);
			if (cipher != null)
				//进行解密返回utf-8类型的字符串
				return new String(cipher.doFinal(b), "UTF-8");
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	public static String byteToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length);
		String sTemp;
		for (int i = 0; i < bytes.length; i++) {
			sTemp = Integer.toHexString(0xFF & bytes[i]);
			if (sTemp.length() < 2)
				sb.append(0);
//			sb.append(sTemp.toUpperCase());
			sb.append(sTemp);
		}
		return sb.toString();
	}
	
	/**
	* @author xupeng
	* @date 2020-12-10 13:13:35
	* @Description:TODO 微信encryptedData解密
	* @param encryptedData
	* @param sessionKey
	* @param iv
	* @return JSONObject
	* @throws
	 */
	public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
 
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
//            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES,"BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                JSONObject parseObject = JSONObject.parseObject(result);
                String unionId = parseObject.getString("unionId");
                String openId = parseObject.getString("openId");
                if (StringUtils.isNotBlank(unionId)) {
                	parseObject.put("unionid", unionId);
				}
                if (StringUtils.isNotBlank(openId)) {
                	parseObject.put("openid", openId);
                }
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
        	log.error("微信encryptedData解密失败：encryptedData，sessionKey，iv分别为：");
        	log.error(encryptedData);
        	log.error(sessionKey);
        	log.error(iv);
            e.printStackTrace();
        }
        return null;
    }
	
	public static void main(String[] args) {
		String encryptedData = "OjvNV/KFgryM7DhpmqA5FFEYD2LMHVzi5GSOvwShDATQld/1SPzrdXtOXwNA0b6wZZPdoGApjAqPyCFZZKAAYibjT3lQbbuzTbLBn5t9Xtdg3A7HmKh+++jBy1MH6iJchts2fK0hVmUoIMisR6Lx/pWDHLBhQAJufa6yacqjVC6a5/noGZb3nwxdo2g6TlR338FSiVIv9wXaI6CD5EU9tH1etCsj5PV+n68Tv7aJXKCcS+7pZAbyFgFW3GLVOpQSVbOqqJ4otfXsa85thd0VTjQKXsN277ZH5cUWYellSmpu7L2qu7AiG7fS/IhukFRJN/C9NjvzN0Qp4q6QeekLUfj3RxtiCPUHncioMIggqVT9509rJ0hv4aJ6PbVpUi1NxtPzRvLbX1/tc3YEq3hZkjXs8bHAV0mmlPuGn9vR6ONZJZvz6KuGZ+IURdk5+idEPUmBmj67dsv+bduptfPaoFglCu57Ut1JaRDkap9QFqcI3ObFKVkHoMddaWeqPsA70aOtIDobLa9NWb2t0fqh/A==";
		String sessionKey = "M7soO0oIFUgdnV1KTjv7Vg==";
		String iv = "ygKl6Hwsd/8QsQcFkHHIMA==";
		JSONObject userInfo = EncryptUtil.getUserInfo(encryptedData, sessionKey, iv);
		System.err.println(userInfo.toJSONString());
	}
}
