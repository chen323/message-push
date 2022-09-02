package com.chen.msgpush.utils.aes;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

/**
 * @author chen
 * Description : AES加解密工具类
 */
@Slf4j
public class AESEncrypt {
    private static final Integer DECRYPT_KEY_LENGTH = 32;

    private static final String KEY_ALGORITHM = "AES";
    //算法
    private static final String AES_ECB_PKCS5PADDING_ALGORITHM_STR = "AES/ECB/PKCS5Padding";
    private static final String AES_CBC_PKCS7PADDING_ALGORITHM_STR = "AES/CBC/PKCS7Padding";

    static {
        //break JCE crypto policy limit
        //for Exception:java.security.InvalidKeyException: Illegal key size or default parameters
        try {
            Class<?> clazz = Class.forName("javax.crypto.JceSecurity");
            Field nameField = clazz.getDeclaredField("isRestricted");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

            nameField.setAccessible(true);
            nameField.set(null, Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) {
        if (decryptKey.length() > AESEncrypt.DECRYPT_KEY_LENGTH) {
            decryptKey = decryptKey.substring(0, AESEncrypt.DECRYPT_KEY_LENGTH);
        }
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return ArrayUtils.isEmpty(bytes) ? null : Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     */
    private static byte[] base64Decode(String base64Code) {
        return StringUtils.isEmpty(base64Code) ? null : new Base64().decode(base64Code);
    }


    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     */
    private static byte[] aesEncryptToBytes(String content, String encryptKey) {
        byte[] bytes = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
            kgen.init(128);
            Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5PADDING_ALGORITHM_STR);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));

            bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            log.error("AESEncrypt aesEncryptToBytes error ", e);
        }
        return bytes;
    }


    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     */
    private static String aesEncrypt(String content, String encryptKey) {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     */
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) {
        String result = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
            kgen.init(128);

            Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5PADDING_ALGORITHM_STR);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            result = new String(decryptBytes);
        } catch (Exception e) {
            log.error("AESEncrypt aesDecryptByBytes error ", e);
        }
        return result;
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param sessionKey    解密秘钥
     * @param iv            自定义对称解密算法初始向量 iv
     * @return 解密后的数据
     */
    public static String aesDecryptBySessionKeyAndIv(String encryptedData, String sessionKey, String iv) {
        // 加密数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 解密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);

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
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS7PADDING_ALGORITHM_STR, "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, KEY_ALGORITHM);
            AlgorithmParameters parameters = AlgorithmParameters.getInstance(KEY_ALGORITHM);
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

            byte[] resultByte = cipher.doFinal(dataByte);
            if (ArrayUtils.isNotEmpty(resultByte)) {
                return new String(resultByte);
            }
        } catch (Exception e) {
            log.error("AESEncrypt aesDecryptBySessionKeyAndIv error ", e);
        }
        return null;
    }


    public static void main(String[] args) throws IOException {
        String content = "69a909cc652b5dfdd6cebc9b10ee0a3c";
        log.info("加密前：" + content);
//        String vtoken = "eyJhbGciOiJIUzI1NiJ9.eyJzZXNzaW9uX2lkIjoiRUQ2MjcxNzM0QzAwOTQ1MEFEQjNCRUYzNUExQTJBNDMiLCJleHAiOjE1ODE2NjgzODYsImlhdCI6MTU3OTA3NjM4Nn0.VDN5lBuK0AeVu-7dPbm7iziL-RjjH-m-TEGPXm_NEsw";
//
//        if (vtoken.length() > 32) {
//            vtoken = vtoken.substring(0, 32);
//        }
        String vtoken = "eyJhbGciOiJIUzI1NiJ9.eyJzZXNzaW9";
        log.info("加密密钥和解密密钥：" + vtoken);
        String encrypt = aesEncrypt(content, vtoken);
        log.info("加密后：" + encrypt);
        String decrypt = aesDecrypt(encrypt, vtoken);
//        String decrypt = aesDecrypt("oRPAOh4Y1VUlOBgzk8gK2R2M/nI6ZCJ4PQfJfd5YebKyY8hP6nWfFyx77c1aGlRu+y4K0=", vtoken);
        log.info("解密后：" + decrypt);

        String encryptedDataEncode = "OkQySaxxYhum7REYI4frScaygjuetYww3r8pyRPmWPQ6f7JtBjyjBIM%2FPhECHEkOC8TMLVLVn3mFYPvD250Z6Zwzu3QdCrgbowkAzVZVg8koF1UqQQUj8MhA02N%2F7AS5vQbnzSMMS6WocRU92NETJc%2B6tQzRdCQ8vDLS35M2v7ineHHEpi4%2FwFwZ2qej3eewcIAoCXx1w4K2LbWk7e8%2BRA%3D%3D";
        String sessionKeyEncode = "Y4v5ivp06bDFFssPUIj2sw%3D%3D";
        String ivEncode = "hn9XpCFcVOPtFmUADdBprA%3D%3D";
        String encryptedData = URLDecoder.decode(encryptedDataEncode, CharEncoding.UTF_8);
        String sessionKey = URLDecoder.decode(sessionKeyEncode, CharEncoding.UTF_8);
        String iv = URLDecoder.decode(ivEncode, CharEncoding.UTF_8);

        String result = aesDecryptBySessionKeyAndIv(encryptedData, sessionKey, iv);
    }
}
