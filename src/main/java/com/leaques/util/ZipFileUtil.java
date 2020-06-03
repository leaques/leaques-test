package com.leaques.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class ZipFileUtil {
    public static void main(String[] args) throws IOException {
        unZip("D:\\20200602temp\\apache-maven-3.6.1.zip");
    }


    public static void unZip(String sourceFilename) throws IOException {
        unZip(new File(sourceFilename));
    }

    /**
     * 将sourceFile解压到targetDir
     *
     * @param sourceFile
     * @throws RuntimeException
     */
    public static void unZip(File sourceFile) throws IOException {
        long start = System.currentTimeMillis();
        if (!sourceFile.exists()) {
            throw new FileNotFoundException("cannot find the file = " + sourceFile.getPath());
        }
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(sourceFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                File dirPath = new File(sourceFile.getParentFile(), entry.getName());
                if (entry.isDirectory()) {
                    createDirIfNotExist(dirPath);
                } else {
                    createFileIfNotExist(dirPath);
                    InputStream is = null;
                    FileOutputStream fos = null;
                    try {
                        is = zipFile.getInputStream(entry);
                        fos = new FileOutputStream(dirPath);
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    } finally {
                        try {
                            fos.close();
                        } catch (Exception e) {
                            log.warn("close FileOutputStream exception", e);
                        }
                        try {
                            is.close();
                        } catch (Exception e) {
                            log.warn("close InputStream exception", e);
                        }
                    }
                }
            }
            log.info("解压完成，耗时：" + (System.currentTimeMillis() - start) + " ms");
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    log.warn("close zipFile exception", e);
                }
            }
        }
    }

    public static void createDirIfNotExist(String path) {
        File file = new File(path);
        createDirIfNotExist(file);
    }

    public static void createDirIfNotExist(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createFileIfNotExist(File file) throws IOException {
        createParentDirIfNotExist(file);
        file.createNewFile();
    }

//    public static void createParentDirIfNotExist(String filename){
//        File file = new File(filename);
//        createParentDirIfNotExist(file);
//    }

    public static void createParentDirIfNotExist(File file) {
        createDirIfNotExist(file.getParentFile());
    }

}
