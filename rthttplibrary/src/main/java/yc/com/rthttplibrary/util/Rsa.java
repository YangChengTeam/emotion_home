/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package yc.com.rthttplibrary.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class Rsa {
    private static final String ALGORITHM = "RSA";

    private static final int MAX_ENCRYPT_BLOCK = 117;



    private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey);

        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    public static byte[] encrypt2(String content, String key) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, key);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String encrypt3(String content, String key) throws Exception {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, key);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte plaintext[] = content.getBytes();
            byte[] output = cipher.doFinal(plaintext);
            int inputLen = output.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(output, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(output, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Log.e("TAG", "encrypt3: " + EncryptUtil.decryptByPrivateKey(encryptedData, GoagalInfo.get().getPublicKey(privateKey)));
            String result = Base64.encode(encryptedData);
//            String result = new String(android.util.Base64.encode(encryptedData, android.util.Base64.NO_WRAP));
            LogUtil.msg("客户端请求加密数据-> result->" + result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String content, String key) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, key);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);
            return Base64.encode(output);
        } catch (Exception e) {
            LogUtil.msg("RSA加密码出错->" + e.getMessage());
            return null;
        }
    }






}
