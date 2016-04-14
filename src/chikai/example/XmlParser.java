package chikai.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Chikai on 16/4/13.
 * 1 BufferedReader 生成 stream
 * 2 InputSource 通过 stream  生成
 * 3 DocumentBuilderFactory 先初始化
 * 4 DocumentBuilder 通过 DocumentBuilderFactory 生成
 * 5 Document 就是最后储存xml文件内容的地方， 通过DocumentBuilder 转化 InputSource 生成
 * 6 通过对 Document node 的操作， 可以从父节点 一直 访问到 子节点
 * 7 因为xml 文件中有“＃TEXT” node 的存在， 所以 element.getChildNodes(); 数值会高出预期
 *   这时候 再次便利的时候就需要使用  if (childNode.getNodeType() == Node.ELEMENT_NODE)来剔除干扰
 */
public class XmlParser {




    private ArrayList<String> header = new ArrayList<String>();
    private void setHeader()
    {
        // {"name", "age", "sex"}
        this.header.add("name");
        this.header.add("age");
        this.header.add("sex");
    }

    private Logger logger = Logger.getLogger(getClass().getName());
    private InputStream rowXmlData;
    private ArrayList<ArrayList<String>> rowsData = new ArrayList<>();




    public XmlParser(InputStream rowXmlData)
    {
        this.rowXmlData = rowXmlData;
        setHeader();
    }

    /**
     * 1. InputStream -> InputSource
     * 2. DocumentBuilderFactory -> DocumentBuilder
     * 3. Document = DocumentBuilder + InputSource
     * **/
    private Document generateDoc (InputStream rowXmlData)
    {
        //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(rowXmlData));

        InputSource inputSource = new InputSource(rowXmlData);
        DocumentBuilder documentBuilder = null;
        Document document = null;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try
        {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(inputSource);
            document.getDocumentElement().normalize();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            logger.log(Level.SEVERE, e.getMessage());
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return  document;
    }

    public void generateRowsDataStaticElement()
    {
        rowsData.clear();

        Document document = generateDoc(rowXmlData);
        if (document!=null)
        {
            NodeList targetNodes = document.getElementsByTagName("user");

            for (int i = 0; i < targetNodes.getLength(); i++)
            {
                Node node = targetNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;

//                    System.out.println("node ==" +element.getTagName());
//                    System.out.println("node ==" +element.getNodeName());
//                    System.out.println("node ==" +element.getAttribute("id"));
//                    System.out.println("node ==" +element.getNodeValue());
//                    NodeList children = element.getChildNodes();
//                    System.out.println("node c ==" +children.getLength());

                    ArrayList<String> row = new ArrayList<String>();

                    //Method 1
//                    for (String childTag: this.header)
//                    {
//                        // NodeList nodeList = element.getElementsByTagName(childTag);
//                        //System.out.println(childTag+" === "+nodeList.getLength());
//                        row.add(element.getElementsByTagName(childTag).item(0).getTextContent());
//                    }

                    //Method 2
                   NodeList children = element.getChildNodes();
                    for (int j = 0; j < children.getLength();j++)
                    {
                        Node mNode = children.item(j);
                        if (mNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            row.add(mNode.getTextContent());
                        }
                    }

                    rowsData.add(row);
                }
            }

        }

    }

    public void gerateRowsDataForALLElement()
    {

    }

//                            NodeList users = document.getElementsByTagName("*");
//                            System.out.println("node number" + users.getLength());
//                            for (int i = 0; i < users.getLength(); i++)
//                            {
//                                Node node = users.item(i);
//                                if (node.getNodeType() == Node.ELEMENT_NODE)
//                                {
//                                    System.out.println(node.getNodeName());
//                                    if (node.getTextContent()!= null && node.getChildNodes().getLength() <=1)
//                                    {
//                                        System.out.println("content = " + node.getTextContent());
//                                        NodeList lastnode = node.getChildNodes();
//
//                                        for (int j = 0; j< lastnode.getLength(); j++)
//                                        {
//                                            System.out.println(lastnode.item(j).getNodeType());
//                                        }
//
//                                    }
//                                }
//                            }

    //User recursion wehter do someting
    private void doSomething(Node node)
    {

        System.out.println(node.getNodeName());

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i< nodeList.getLength();i++)
        {
            Node currentNode = nodeList.item(i);

            if (currentNode.getNodeType() == Node.ELEMENT_NODE)
            {
                doSomething(currentNode);
            }
        }
    }

    public ArrayList<String> getHeader() {
        return header;
    }

    public ArrayList<ArrayList<String>> getRowsData() {
        return rowsData;
    }


}
