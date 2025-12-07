package com.oj.platform.libs.env;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;

public class EnvLoader {
    public static void load(String path, String filename) {
        File rootDir = new File(path);
        File envFile = new File(rootDir, filename);

        if (!envFile.exists()) {
            System.out.println("[dotenv] .env not found at: " + envFile.getAbsolutePath());
            return;
        }

        Dotenv dotenv = Dotenv.configure()
                .directory(rootDir.getAbsolutePath())
                .filename(filename)
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(e -> {
            if (System.getProperty(e.getKey()) == null) {
                System.setProperty(e.getKey(), e.getValue());
            }
        });

        System.out.println("[dotenv] Loaded entries: " + dotenv.entries().size());
    }
}
