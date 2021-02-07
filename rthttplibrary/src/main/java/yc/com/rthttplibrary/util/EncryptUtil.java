package yc.com.rthttplibrary.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;


public class EncryptUtil {

    ///< rsa 分段加密
    public static String rsa(String publickey, String jsonStr) {
        String key = publickey;
        LogUtil.msg("publickey->" + Md5.md5(key));
        String result = null;
        String[] strs = sectionStr(jsonStr);
        if (strs == null) {
            result = Rsa.encrypt(jsonStr, key);
            LogUtil.msg("客户端请求加密数据->" + result);
        } else {
            try {
                List<byte[]> bytes = new ArrayList<>();
                int len = 0;
                for (int i = 0; i < strs.length; i++) {
                    LogUtil.msg(i + "." + strs[i]);
                    byte[] output = Rsa.encrypt2(strs[i], key);
                    len += output.length;
                    bytes.add(output);
                }
                byte[] dest = new byte[len];
                for (int i = 0, start = 0; i < bytes.size(); i++) {
                    byte[] tmp = bytes.get(i);
                    System.arraycopy(tmp, 0, dest, start, tmp.length);
                    start += tmp.length;
                }
                result = Base64.encode(dest);
                LogUtil.msg("客户端请求加密数据->" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    ///< rsa字符分段
    @SuppressWarnings("null")
    public static String[] sectionStr(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String[] strs = null;
        int length = str.length();
        if (length > 128) {
            int len = length / 128 + (length % 128 > 0 ? 1 : 0);
            strs = new String[len];
            for (int i = 0, j = 0; i < length; i += 1) {
                int start = i * 128;
                int last = (i + 1) * 128;
                if (last > length) {
                    last = length;
                }
                if (j >= len) {
                    break;
                }
                strs[j++] = str.substring(start, last);
            }
        }
        return strs;
    }

    ///< 数据压缩
    public static byte[] compress(String data) {
        try {
            byte[] bytes = data.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            compress(bais, baos);
            byte[] output = baos.toByteArray();

            baos.flush();
            baos.close();
            bais.close();

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void compress(InputStream is, OutputStream os)
            throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        int count;
        byte[] data = new byte[1024];
        while ((count = is.read(data, 0, data.length)) != -1) {
            gos.write(data, 0, count);
        }
        gos.finish();
        gos.close();
    }

    ///< 数据解压
    public static String unzip(InputStream in) {
        // Open the compressed stream
        GZIPInputStream gin;
        try {
            if (in == null) {
                LogUtil.msg("服务器没有返回数据->null");
                return null;
            }
            gin = new GZIPInputStream(new BufferedInputStream(in));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // Transfer bytes from the compressed stream to the output stream
            byte[] buf = new byte[1024];
            int len;
            while ((len = gin.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            gin.close();
            out.close();
            String result = new String(out.toByteArray());
            LogUtil.msg("服务器返回数据->" + result);
            return result;
        } catch (IOException e) {
            LogUtil.msg("服务器返回数据解压异常:" + e.getMessage(), LogUtil.W);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 私钥解密
     *
     * @param
     * @param privateKeyStr
     * @return
     */
    public static String decryptByPrivateKey(byte[] data, String privateKeyStr) {
        try {
            // 对私钥解密
            byte[] privateKeyBytes = Base64.decode(privateKeyStr);

            // 获得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

//            X509EncodedKeySpec x509 = new X509EncodedKeySpec(privateKeyBytes);
            // 获得待解密数据
//            byte[] data = Base64.decode(encryptedStr);
//            byte[] data = android.util.Base64.decode(encryptedStr.getBytes(), android.util.Base64.NO_WRAP);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据解密
            String algorithm = factory.getAlgorithm();
//            Log.e("TAG", "decryptByPrivateKey: " + algorithm);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 返回UTF-8编码的解密信息
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(data, offSet, 128);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * 128;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
