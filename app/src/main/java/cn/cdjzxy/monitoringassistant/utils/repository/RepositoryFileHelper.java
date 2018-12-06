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

import cn.cdjzxy.monitoringassistant.mvp.model.entity.repository.RepositoryFile;

/**
 * 知识库文件Data处理帮助类
 */
public class RepositoryFileHelper {

    public static List<RepositoryFile> getRepositoryFiles(String xml) {
        List<RepositoryFile> fileItems = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();//①获得DOM解析器的工厂示例:
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();//②从Dom工厂中获得dom解析器
            Document doc = dbBuilder.parse(new ByteArrayInputStream(xml.getBytes()));//③把要解析的xml文件读入Dom解析器
            NodeList nList = doc.getElementsByTagName("FileItems");//④得到文档中名称为Org的元素的结点列表
            for (int i = 0; i < nList.getLength(); i++) {//⑤遍历该集合,显示集合中的元素以及子元素的名字
                Element element = (Element) nList.item(i);//开始解析
                NodeList childNoList = element.getChildNodes();   //获取集合
                for (int j = 0; j < childNoList.getLength(); j++) {
                    Node childNode = childNoList.item(j);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {//判断子note类型是否为元素Note
                        Element childElement = (Element) childNode;
                        if (childElement.getNodeName().equals("Item")) {
                            fileItems.add(createFileItem(childElement));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileItems;
    }


    /**
     * 创建文件
     *
     * @param element
     * @return
     */
    private static RepositoryFile createFileItem(Element element) {
        RepositoryFile fileItem = new RepositoryFile();
        fileItem.setName(element.getAttribute("Name"));
        fileItem.setFileKey(element.getAttribute("FileKey"));
        fileItem.setSize(element.getAttribute("Size"));
        fileItem.setModifyDatetime(element.getAttribute("ModifyDatetime"));
        fileItem.setUploadDatetime(element.getAttribute("UploadDatetime"));
        fileItem.setUploader(element.getAttribute("Uploader"));
        fileItem.setIsAudited(element.getAttribute("IsAudited"));
        fileItem.setEncrypted(element.getAttribute("Encrypted"));
        fileItem.setInRecycle(element.getAttribute("InRecycle"));
        fileItem.setDiskIndex(element.getAttribute("DiskIndex"));
        fileItem.setPath(element.getAttribute("Path"));
        fileItem.setSC(element.getAttribute("SC"));
        fileItem.setCheckoutUserId(element.getAttribute("CheckoutUserId"));
        fileItem.setIsMapping(element.getAttribute("IsMapping"));
        return fileItem;
    }
}
