package yang.micro.uitl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.antherd.smcrypto.sm2.Sm2;
import com.antherd.smcrypto.sm4.Sm4;
import yang.micro.exception.SDKException;
import yang.micro.exception.SDKExceptionEnums;
import yang.micro.param.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 加解密流程
 *   因为是考虑到B/S架构 ，无法使用正常的公私钥对  因此设计密钥方式
 *   使用对称密钥加密 添加随机密钥 没加密
 */
public class GMEncryptionUtils {

    /**
     * 先验签 再解密
     * @param randomKey 随机密钥
     * @param privateKey 私钥
     * @param enMsg 明文报文
     * @return
     */
    public static String decryptFromH5Date( String privateKey ,String randomKey ,String enMsg) throws SDKException, IOException {

        JSONObject jsonObject = JSON.parseObject(enMsg);
        String sgn = SDK_SecurityUtils.decryptByBASE64( jsonObject.getString(Constants.SCRT_TRAN_MESSAGE_SGN_NAME), StandardCharsets.UTF_8);
        String enDate = SDK_SecurityUtils.decryptByBASE64( jsonObject.getString(Constants.SCRT_TRAN_MESSAGE_DATA_NAME), StandardCharsets.UTF_8);
        //① 验签 随机密钥对密文验签
        if(!Sm4.encrypt(enDate, randomKey).equals(sgn)){
            throw new SDKException(SDKExceptionEnums.CHERSA_ERROR);
        }
        //② 解密 私钥对密文进行解密
        return Sm2.doDecrypt(enDate,privateKey);
    }

    /**
     * 先验签 再解密
     * @param randomKey 随机密钥
     * @param publicKey 私钥
     * @param msg 明文报文
     * @return
     */
    public static String encryptFromH5Date( String publicKey ,String randomKey ,String msg) throws SDKException, IOException {
        //① 加密 公钥加密
        String SecretData = Sm2.doEncrypt(msg,publicKey);
        //② 随机密钥加签
        String SecretSign = SDK_SecurityUtils.encryptByBASE64(Sm4.encrypt(SecretData,randomKey).getBytes(StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SecretData",SDK_SecurityUtils.encryptByBASE64(SecretData.getBytes(StandardCharsets.UTF_8)));
        jsonObject.put("SecretSign",SecretSign);
        return jsonObject.toJSONString();
    }

    /**
     *
     * @param publicKey
     * @param randomKey
     * @param enMsg
     * @return
     * @throws SDKException
     * @throws IOException
     */
    public static String decryptToH5Date( String publicKey ,String randomKey ,String enMsg) throws SDKException, IOException {
        JSONObject jsonObject = JSON.parseObject(enMsg);
        String sgn = SDK_SecurityUtils.decryptByBASE64( jsonObject.getString(Constants.SCRT_TRAN_MESSAGE_SGN_NAME), StandardCharsets.UTF_8);
        String enData = SDK_SecurityUtils.decryptByBASE64( jsonObject.getString(Constants.SCRT_TRAN_MESSAGE_DATA_NAME), StandardCharsets.UTF_8);

        //① 随机密钥解密
        String data = Sm4.decrypt(enData, randomKey);
        //② 公钥验签
        if(!Sm2.doVerifySignature(data,sgn,publicKey)){
            throw new SDKException(SDKExceptionEnums.CHERSA_ERROR);
        }
        return data;
    }

    /**
     * 加密 服务端和H5端加密算法不一致
     * @param privateKey 加签
     * @param randomKey 对称加密
     * @param data 明文
     * @return
     * @throws SDKException
     * @throws IOException
     */
    public static String encryptToH5Date( String privateKey ,String randomKey ,String data) throws SDKException, IOException {

        //① 随机密钥加密
        String SecretData = SDK_SecurityUtils.encryptByBASE64( Sm4.encrypt(data,randomKey).getBytes(StandardCharsets.UTF_8));
        //② 私钥加签
        String SecretSign = SDK_SecurityUtils.encryptByBASE64(Sm2.doSignature(data,privateKey).getBytes(StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SecretData",SecretData);
        jsonObject.put("SecretSign",SecretSign);
        return jsonObject.toJSONString();
    }

//
//        public static void main(String[] args) throws SDKException, IOException {
//        String privateKey = "ecadccdddad2cd710f41ac21c8b885611e922fb498e5aa8c9c84917d8efbb59a";
//        String publicKey = "04cff340b22c038aed1401e12f851b97c197472c16cedd0633c258f97569c0fce0c784ce786ec722300f50c2ad000eccbb1e1375dd00bb3a2abd5186513e6afa67";
//
//        String str = "{\"id\":\"yang\",\"name\":\"le\"}";
//        String key = SDK_SecurityUtils.get32RandomKey(); //随机密钥
//            System.out.println("随机密钥为："+key);
//            String enDate = encryptFromH5Date(publicKey, key, str);
//            System.out.println("加密的信息为："+enDate);
//            String data = decryptFromH5Date(privateKey, key, enDate);
//            System.out.println("解密后的信息为："+data);
//
//            String s = encryptToH5Date(privateKey, key, str);
//            System.out.println("加密后的数据"+s);
//            System.out.println("解密后的数据"+decryptToH5Date(publicKey, key, s));
//
//        }
}
