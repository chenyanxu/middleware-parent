package com.kalix.middleware.jwt.biz;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class FileUtil {
    public static String loadFile(String file) {
        try {
            InputStream stream = FileUtil.class.getClassLoader().getResourceAsStream(file);
            return IOUtils.toString(stream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
