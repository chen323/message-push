package com.chen.msgpush.utils;

import com.chen.msgpush.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author chen
 */
@Slf4j
public class FileUtil {

    private static final String FILE_IMAGE_TYPE = ",*.bmp,,*.jpeg,*.png,*.gif,*.jpg,*.zip";

    private FileUtil() {

    }

    /**
     * 判断文件是否是图片
     *
     * @param fileName 文件名
     * @return 是否为图片
     */
    public static boolean isImageFile(String fileName) {

        if (StringUtils.isBlank(fileName)) {
            return false;
        }

        int index = fileName.lastIndexOf(SystemConstant.DOT_SEPARATOR);
        if (index <= 0) {
            return false;
        }

        String suffix = fileName.substring(index, fileName.length());
        return FILE_IMAGE_TYPE.contains(suffix.toLowerCase());
    }

    /**
     * 转换文件大小
     *
     * @param fileS 文件大小
     * @return 转换后的文件大小
     */
    public static String formatFileSize(long fileS) {
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = SystemConstant.twoDecimalFormat.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = SystemConstant.twoDecimalFormat.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = SystemConstant.twoDecimalFormat.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = SystemConstant.twoDecimalFormat.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 网络资源转file, 用完以后必须删除该临时文件
     *
     * @param fileUrl 资源地址
     * @param suffix  文件后缀
     * @return 临时文件
     */
    public static File urlToFile(String fileUrl, String suffix) {
        String path = System.getProperty("user.dir");
        File upload = new File(path, "tmp");
        if (!upload.exists()) {
            upload.mkdirs();
        }

        File tmpFile = null;
        try {
            tmpFile = urlToFile(fileUrl, suffix, upload);
        } catch (IOException e) {
            log.error("FileUtil urlToFile IOException, ", e);
        }
        return tmpFile;
    }

    /**
     * 网络资源转file, 用完以后必须删除该临时文件
     *
     * @param fileUrl 资源地址
     * @param suffix  文件后缀
     * @param upload  临时文件路径
     * @return 临时文件
     */
    private static File urlToFile(String fileUrl, String suffix, File upload) throws IOException {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")) + suffix;
        File savedFile = new File(upload.getAbsolutePath() + fileName);
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (InputStream openStream = connection.getInputStream();
             FileOutputStream downloadFile = new FileOutputStream(savedFile)) {

            int index;
            byte[] bytes = new byte[1024];
            while ((index = openStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
        } catch (Exception e) {
            log.error("FileUtil urlToFile Exception, ", e);
        }
        return savedFile;
    }
}
