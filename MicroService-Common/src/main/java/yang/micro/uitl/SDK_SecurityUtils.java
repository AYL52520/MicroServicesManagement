package yang.micro.uitl;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * @Classname SDK_Security /Utils
 * 加解密封装类
 * @Version 1.0.0
 * @Date 2023/2/9 9:44
 * @Created by yangle
 *
 *
/**
 * 国密-SDK 调用类
 */
public class SDK_SecurityUtils {

    private static Random random = new Random();

    /**
     * 报文进行base64加密处理
     */
    public static String encryptByBASE64(byte key[]) {
        return (new BASE64Encoder()).encodeBuffer(key).replaceAll("\r\n","").replaceAll("\n", "");
    }

    /**
     *
     * @param key 加密密钥
     * @param charset 编码格式
     * @return 加密之后的字节数组
     */
    public static byte[] encryptByBASE64(byte key[] , Charset charset) {
        return (new BASE64Encoder()).encodeBuffer(key).replaceAll("\r\n","").replaceAll("\n", "").getBytes(charset);
    }
    /**
     * base64解密
     * @param key
     * @param charset 编码格式
     * @return 解密后的字符串
     */
    public static String decryptByBASE64(String key, Charset charset) throws IOException {
        return new String((new BASE64Decoder()).decodeBuffer(key.replaceAll("\r\n","").replaceAll("\n", "")),charset);
    }

    /**
     * base64解密
     * @param key
     * @return 加密之后的字节数组[]
     * @throws IOException
     */
    public static byte[] decryptByBASE64(String key) throws IOException {
        return (new BASE64Decoder()).decodeBuffer(key.replaceAll("\r\n","").replaceAll("\n", ""));
    }

    /**
     * 生成16位AES随机密钥
     * @return String
     */
    public static String get16RandomKey(){
        long longValue = random.nextLong();
        return String.format("%016X", longValue);
    }

    /**
     * 生成32位AES随机密钥
     * @return String
     */
    public static String get32RandomKey(){
        long longValue1 = random.nextLong();
        long longValue2 = random.nextLong();
        return String.format("%016X", longValue1)+String.format("%016X", longValue2);
    }

}
