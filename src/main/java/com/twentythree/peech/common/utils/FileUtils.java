package com.twentythree.peech.common.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class FileUtils {

    public static File createAudioFileFromBase64EncodedString(String base64Encode) {
        byte[] audioBytes = Base64.getDecoder().decode(base64Encode);

        String fileName = UUID.randomUUID().toString() + ".webm";

        ClassPathResource classPathResource = new ClassPathResource(fileName);
        File file = new File(classPathResource.getPath());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            file.createNewFile();
            fos.write(audioBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
