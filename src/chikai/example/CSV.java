package chikai.example;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Chikai on 16/4/13.
 * CSV Class provide the function to
 *          1. write row XML data into csv form
 *          2. store the file to relevant place based on parameter
 */
public class CSV
{

    private static final String COMMA = ",";
    private static final String NEXTLINE = "\n";
    private Logger logger = Logger.getLogger(getClass().getName());
    private String csvFileName;
    private ArrayList<String> csvHeader;
    private ArrayList<ArrayList<String>> csvRowsData;
    private String storePath;

    public CSV (String storePath,String csvFileName, ArrayList<String> csvHeader,ArrayList<ArrayList<String>> csvRowsData)
    {
        this.storePath = storePath;
        this.csvFileName = csvFileName;
        this.csvHeader = csvHeader;
        this.csvRowsData = csvRowsData;
    }

    public void saveFile()
    {
        String fileSaveName = storePath + "/" + csvFileName + ".csv";

        File file = new File(fileSaveName);

        if (file.exists())
        {
            file.delete();
        }

        /**
         * other option
         * **/
//        if (file.exists()) {
//            Random rand = new Random();
//            fileSaveName += String.valueOf(rand.nextFloat());
//        }

        try
        {
            //open writer start to write
            FileWriter fileWriter = new FileWriter(fileSaveName);

            //write head first
            fileWriter.append(StringUtils.join(csvHeader, COMMA));
            fileWriter.append(NEXTLINE);

            //write the content
            for (ArrayList<String> row: csvRowsData)
            {
                fileWriter.append(StringUtils.join(row,COMMA));
                fileWriter.append(NEXTLINE);
            }

            //write file flush and close
            fileWriter.flush();
            fileWriter.close();

            logger.log(Level.INFO, "Saved csv file to " + fileSaveName);
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, "Generate csv File: " + e.getMessage());

        }

    }
}
