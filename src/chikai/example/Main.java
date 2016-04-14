package chikai.example;

import org.apache.commons.cli.*;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;


/***
 * Class ReadZip
 * -- > List of RawXmlFile Object
 * --> Use XmlParser parse RawXmlFile generate CSV data
 * --> Use CSV Class to generate CSV file using CSV data
 * **/
public class Main {

    public static void main(String[] args) throws Exception
    {

//        String path = "/Users/Chikai/testdata2.zip";
//
//
        /**
         * use org.apache.commons.cli set command line option
         * */
        Options options = new Options();
        options.addOption(Option.builder("f")
                .argName("input_files")
                .hasArgs()
                .desc("input file list")
                .build());
        options.addOption("show", false, "show csv content");
        options.addOption("save", false, "save csv file");
        options.addOption("help", false, "show help");

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help") || args.length == 0) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ZipFileTransform", options);
                return;
            }

            if (cmd.hasOption("f"))
            {
                //read input files parameters from command line
                String[] inputFiles = cmd.getOptionValues("f");

                for(String inputFile : inputFiles)
                {
                    if (!inputFile.startsWith("-"))
                    {
                        System.out.println("success read in commandline  == "+inputFile);//todo

                        ReadZip mReadZip = new ReadZip(inputFile);

                        ArrayList<RawXmlFile> rawXmlFiles = mReadZip.readAllFilesInZip();

                        if (rawXmlFiles != null)
                        {
                            System.out.println("parent pth  == " + mReadZip.getZipFileParentPath());

                            /**Create new folder to store future files**/
                            String newFolderPath = mReadZip.getZipFileParentPath()+"/"+mReadZip.getZipFileName().replace(".zip","");
                            try
                            {
                                File filePath = new File(newFolderPath);
                                if (!filePath.exists())
                                {
                                    filePath.mkdir();
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println("create new folder fail");
                                e.printStackTrace();
                            }

                            /***
                             * 1. Call the xmlParser raw xml file
                             * 2. CSV to create new csv file
                             *
                             * **/
                            for (RawXmlFile xmlFile : rawXmlFiles)
                            {
                                System.out.println("xml name  == " + xmlFile.fileName);
                                XmlParser xmlParser = new XmlParser(xmlFile.getFileContent());
                                xmlParser.generateRowsDataStaticElement();

                                CSV csv = new CSV(newFolderPath,xmlFile.getFileName().replace(".xml",""),xmlParser.getHeader(),xmlParser.getRowsData());
                                csv.saveFile();
                            }
                        }
                    }

                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
