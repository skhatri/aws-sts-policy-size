package com.github.skhatri.aws;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataGen {
    public static void main(String[] args) {

        List<String> folders = List.of("aws-sts-data-files", "aws-sts-data-files/archive");

        List<String> domains = List.of("sports", "finance", "science", "technology", "worldnews", "deals", "questions");
        LocalDate now = LocalDate.now();
        for (String f : folders) {
            for (String domain : domains) {
                for (int i = 0; i < 365; i++) {
                    LocalDate eodDate = now.plusDays(-i);
                    String dateValue = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(eodDate);
                    String folder = String.format("date=%s", dateValue);
                    File targetDir = new File(String.format("%s/%s/%s", f, domain, folder));
                    if (!targetDir.exists()) {
                        targetDir.mkdirs();
                    }
                    try {
                        Files.copy(Paths.get("assets/sample.parquet"), new File(targetDir, "weather.parquet").toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception exc) {
                        System.err.println("error copying data" + exc);
                    }
                }
            }
        }
    }
}
