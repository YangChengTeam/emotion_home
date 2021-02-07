package yc.com.rthttplibrary.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PathUtil {
    public static String getConfigPath(Context context) {
        String baseDir = makeBaseDir(context);
        File dir = new File(baseDir + "/config");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

    public static String createDir(Context context, String dirName) {
        String baseDir = makeBaseDir(context);
        File dir = new File(baseDir + dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

    private static String makeBaseDir(Context context) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + context.getPackageName());
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

    public static File copyFileToPath(Context context, Uri uri, String path) {
        InputStream inputStream = null;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        File targetFile = null;
        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    int read;
                    byte[] buffer = new byte[8 * 1024];
                    //自己定义拷贝文件路径
                    targetFile = new File(path);
                    OutputStream outputStream = new FileOutputStream(targetFile);
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return targetFile;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }
}
