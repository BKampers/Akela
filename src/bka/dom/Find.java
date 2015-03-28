package bka.dom;

import org.w3c.dom.*;

/**
 *
 * @author bkampers
 */
public class Find {
    
    
    public static NodeList nodeList(Node node, String name) {
        return nodeList(node.getChildNodes(), name);
    }


    public static NodeList nodeList(NodeList nodeList, String name) {
        Node node = node(nodeList, name);
        return node != null ? node.getChildNodes() : null;
    }


    public static String content(Node node, String name) {
        String text = null;
        NodeList nodeList = node.getChildNodes();
        Node nameNode = node(nodeList, name);
        if (nameNode != null) {
            //text = nameNode.getTextContent();  // JDK 1.6
            text = textValue(nameNode); // JDK 1.4
//            Node firstChild = nameNode.getFirstChild();
//            if (firstChild != null) {
//                text = firstChild.getNodeValue();
//            }
        }
        return text;
    }


    public static Node node(NodeList nodeList, String name) {
        Node node = null;
        int i = 0;
        while (node == null && i < nodeList.getLength()) {
            Node n = nodeList.item(i);
            if (name.equals(n.getNodeName())) {
                node = n;
            }
            i++;
        }
        return node;
    }

     
    public static String attributeValue(Node node, String name) {
        String value = null;
        NamedNodeMap map = node.getAttributes();
        if (map != null) {
            Node attributeNode = map.getNamedItem(name);
            if (attributeNode != null) {
                value = attributeNode.getNodeValue();
            }
        }
        return value;
    }

     
    public static String textValue(Node node) {
        String text = null;
        Node firstChild = node.getFirstChild();
        if (firstChild != null) {
            text = firstChild.getNodeValue();
        }
        return text;
     }

}
