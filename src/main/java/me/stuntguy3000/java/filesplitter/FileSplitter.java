package me.stuntguy3000.java.filesplitter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSplitter {
    public static void main(String[] args) {
        int mbToSplit = 209715200; // 200MB
        String folderID = "FileBatch200MB_";
        File thisFolder = null;

        List<List<File>> fileBatches = new ArrayList<>();

        thisFolder = new File(".");

        long currentAmountProcessed = 0;
        List<File> currentFileBatch = new ArrayList<>();

        for (File file : thisFolder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".jpg");
            }
        })) {

            currentFileBatch.add(file);

            // Get length of file in bytes
            currentAmountProcessed = currentAmountProcessed + file.length();

            System.out.println("Processing Image... " + file.getName());

            if (currentAmountProcessed >= mbToSplit) {
                fileBatches.add(new ArrayList<>(currentFileBatch));
                currentFileBatch.clear();
                currentAmountProcessed = 0;
            }
        }

        System.out.println("\nProcessing " + fileBatches.size() + " batch(s)...");
        int batchNumber = 1;
        for (List<File> fileBatch : fileBatches) {
            System.out.println("\nProcessing batch " + batchNumber);

            File targetFolder = new File(folderID + batchNumber);
            targetFolder.mkdirs();

            for (File file : fileBatch) {
                try {
                    FileUtils.copyFileToDirectory(file, targetFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            batchNumber++;
        }
    }
}
