package chikai.example;

import java.io.InputStream;

/**
 * Created by Chikai on 16/4/13.
 */
public class RawXmlFile
{
    String fileName;
    InputStream fileContent;


    public RawXmlFile(String fileName, InputStream fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getFileContent() {
        return fileContent;
    }
}
