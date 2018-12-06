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

import cn.cdjzxy.monitoringassistant.mvp.model.entity.repository.RepositoryGroup;


/**
 * 知识库小组Data处理帮助类
 */
public class RepositoryGroupHelper {

    public static List<RepositoryGroup> getRepositoryGroups(String xml) {
        List<RepositoryGroup> groups = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();//①获得DOM解析器的工厂示例:
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();//②从Dom工厂中获得dom解析器
            Document doc = dbBuilder.parse(new ByteArrayInputStream(xml.getBytes("GB2312")));//③把要解析的xml文件读入Dom解析器
            NodeList nList = doc.getElementsByTagName("Org");//④得到文档中名称为Org的元素的结点列表
            for (int i = 0; i < nList.getLength(); i++) {//⑤遍历该集合,显示集合中的元素以及子元素的名字
                Element element = (Element) nList.item(i);//开始解析
                NodeList childNoList = element.getChildNodes();   //获取集合
                for (int j = 0; j < childNoList.getLength(); j++) {
                    Node childNode = childNoList.item(j);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {//判断子note类型是否为元素Note
                        Element childElement = (Element) childNode;
                        if (childElement.getNodeName().equals("GroupItem")) {
                            RepositoryGroup group = createGroup(childElement);
                            readElement(childElement, group);
                            groups.add(group);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
    }

    /**
     * 解析文档元素
     *
     * @param element
     * @param group
     */
    private static void readElement(Element element, RepositoryGroup group) {
        NodeList childNodes = element.getChildNodes();   //获取集合
        for (int j = 0; j < childNodes.getLength(); j++) {
            Node childNode = childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {//判断子note类型是否为元素Note
                Element childElement = (Element) childNode;
                if (childElement.getNodeName().equals("GroupItem")) {
                    RepositoryGroup childGroup = createGroup(childElement);
                    readElement(childElement, childGroup);
                    group.getChilds().add(childGroup);
                } else if ("Members".equals(childElement.getNodeName())) {
                    List<RepositoryGroup.Member> members = new ArrayList<>();
                    group.setMembers(members);
                    readElement(childElement, group);
                } else if ("Childs".equals(childElement.getNodeName())) {
                    List<RepositoryGroup> childs = new ArrayList<>();
                    group.setChilds(childs);
                    readElement(childElement, group);
                } else if ("Item".equals(childElement.getNodeName())) {
                    group.getMembers().add(createMember(childElement));
                }
            }
        }
    }

    /**
     * 创建组
     *
     * @param element
     * @return
     */
    private static RepositoryGroup createGroup(Element element) {
        RepositoryGroup group = new RepositoryGroup();
        group.setId(element.getAttribute("Id"));
        group.setName(element.getAttribute("Name"));
        if (element.hasAttribute("FatherId")) {
            group.setFatherId(element.getAttribute("FatherId"));
        } else {
            group.setFatherId("");
        }
        return group;
    }


    /**
     * 创建成员
     *
     * @param element
     * @return
     */
    private static RepositoryGroup.Member createMember(Element element) {
        RepositoryGroup.Member member = new RepositoryGroup.Member();
        member.setId(element.getAttribute("Id"));
        member.setName(element.getAttribute("Name"));
        member.setEmail(element.getAttribute("Email"));
        member.setLasterLoginTm(element.getAttribute("LasterLoginTm"));
        member.setCoreId(element.getAttribute("CoreId"));
        return member;
    }

}
