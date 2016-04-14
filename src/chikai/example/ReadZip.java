package chikai.example;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created by Chikai on 16/4/12.
 *
 * Read the Zip file into memory
 */
public class ReadZip {

    private String zipFilePath;
    private String zipFileParentPath;
    private String zipFileName = null;
    private Logger mLogger = Logger.getLogger(getClass().getName());


    /**
     * Constructor
     * @param  path --> zip File path
     * **/
    public ReadZip (String path)
    {
        zipFilePath = path;
    }

    public ArrayList<RawXmlFile> readAllFilesInZip()
    {
        System.out.println("work 0");
        zipFileName = null;//init

        ArrayList<RawXmlFile> rawXmlFiles = new ArrayList<RawXmlFile>();

        File compressedFile = new File(zipFilePath);
        System.out.println("work 0 ==" + zipFilePath);
        if (compressedFile.exists())
            {
                System.out.println("work 1");
                zipFileName = compressedFile.getName();
                zipFileParentPath = compressedFile.getParent();
                System.out.println("find compress file ="+zipFileName+" : "+zipFileParentPath);
            }
        else
            {
                mLogger.log(Level.SEVERE, "Invalid File Path: " + zipFilePath);
                return null;
            }


        if (!compressedFile.isDirectory())
        {
            try
            {
                ZipFile mZipFile = new ZipFile(compressedFile);//Open the zip file
                Enumeration<ZipEntry> entryEnumerations = (Enumeration<ZipEntry>) mZipFile.entries();
                while(entryEnumerations.hasMoreElements())
                {
                    ZipEntry mZipEntry = entryEnumerations.nextElement();
                    String xmlFileName = mZipEntry.getName();

                    long size = mZipEntry.getSize();
                    if (size>0 && mZipEntry.getName().contains(".xml"))
                    {
                        InputStream rowContent = mZipFile.getInputStream(mZipEntry);
                        RawXmlFile rawXmlFile = new RawXmlFile(xmlFileName,rowContent);
                        rawXmlFiles.add(rawXmlFile);
                    }

                }

            }
            catch (ZipException zipException)
            {
                mLogger.log(Level.SEVERE, "zipExpection: " + zipException.getMessage());
            }
                catch(IOException e)
                {
                    mLogger.log(Level.SEVERE, "IOExpection: " + e.getMessage());
            }
        }
        else
        {
            mLogger.log(Level.SEVERE, "Invalid File Path: " + zipFilePath);
            return null;
        }

        return rawXmlFiles;
    }

    public void oneFileInZip(String fileName)
    {
        //todo something
    }

    public String getZipFilePath()
    {
        return  zipFilePath;
    }

    public String getZipFileName()
    {
        return zipFileName;
    }

    public String getZipFileParentPath() {
        return zipFileParentPath;
    }

}
