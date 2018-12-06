package cn.cdjzxy.monitoringassistant.utils.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.repository.RepositoryFolder;

/**
 * 知识库文件夹Data处理帮助类
 */
public class RepositoryFolderHelper {

    public static List<RepositoryFolder> getRepositoryFolders(String xml) {
        List<RepositoryFolder> folderses = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();//①获得DOM解析器的工厂示例:
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();//②从Dom工厂中获得dom解析器
            Document doc = dbBuilder.parse(new ByteArrayInputStream(xml.getBytes("GB2312")));//③把要解析的xml文件读入Dom解析器
            NodeList nList = doc.getElementsByTagName("Data");//④得到文档中名称为Org的元素的结点列表
            for (int i = 0; i < nList.getLength(); i++) {//⑤遍历该集合,显示集合中的元素以及子元素的名字
                Element element = (Element) nList.item(i);//开始解析
                NodeList childNoList = element.getChildNodes();   //获取集合
                for (int j = 0; j < childNoList.getLength(); j++) {
                    Node childNode = childNoList.item(j);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {//判断子note类型是否为元素Note
                        Element childElement = (Element) childNode;
                        if (childElement.getNodeName().equals("Item")) {
                            RepositoryFolder folder = createFolderAndFile(childElement);
                            readElement(childElement, folder);
                            folderses.add(folder);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folderses;
    }

    /**
     * 解析文档元素
     *
     * @param element
     * @param folder
     */
    private static void readElement(Element element, RepositoryFolder folder) {
        NodeList childNodes = element.getChildNodes();   //获取集合
        for (int j = 0; j < childNodes.getLength(); j++) {
            Node childNode = childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {//判断子note类型是否为元素Note
                Element childElement = (Element) childNode;
                if (childElement.getNodeName().equals("Path")) {
                    folder.setPath(childElement.getFirstChild().getNodeValue());
                } else if ("Item".equals(childElement.getNodeName())) {
                    RepositoryFolder childFolder = createFolderAndFile(childElement);
                    folder.getChilds().add(childFolder);
                    readElement(childElement, childFolder);
                } else if ("Childs".equals(childElement.getNodeName())) {
                    List<RepositoryFolder> childs = new ArrayList<>();
                    folder.setChilds(childs);
                    readElement(childElement, folder);
                }
            }
        }
    }

    /**
     * 创建文件
     *
     * @param element
     * @return
     */
    private static RepositoryFolder createFolderAndFile(Element element) {
        RepositoryFolder folder = new RepositoryFolder();
        folder.setId(element.getAttribute("Id"));
        folder.setFatherId(element.getAttribute("FatherId"));
        return folder;
    }


}
