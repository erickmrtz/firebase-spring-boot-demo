package me.erickmrtz.demo.firebase.util;

import java.util.Objects;
import java.util.UUID;

public class Functions {

    public static String getFileName(String originalName) {
        return String.format(
                "%s%s",
                UUID.randomUUID(),
                Objects.requireNonNull(originalName).substring(originalName.lastIndexOf("."))
        );
    }

    public static String getFileLocation(String fileName) {
        return String.format("stores/%s",fileName);
    }
}
