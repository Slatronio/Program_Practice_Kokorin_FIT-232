package data.extractor;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Component("xml")
public class XmlExtractor implements DataExtractor {
    @Override
    public String extract(String content, String path) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        dbf.setNamespaceAware(false);
        Document doc = dbf.newDocumentBuilder().parse(new org.xml.sax.InputSource(new StringReader(content)));

        Node current = doc.getDocumentElement();
        String pendingTagName = null;

        for (String part : path.split("/")) {
            if (part.isEmpty()) continue;
            if (part.startsWith("[") && part.endsWith("]")) {
                int index = Integer.parseInt(part.substring(1, part.length() - 1));
                current = getNodeByIndex(current, pendingTagName, index);
                if (current == null) throw new IllegalArgumentException("Элемент не найден: " + pendingTagName + "[" + index + "]");
                pendingTagName = null;
            } else {
                if (pendingTagName != null) {
                    current = getFirstChild(current, pendingTagName);
                    if (current == null) throw new IllegalArgumentException("Тег не найден: " + pendingTagName);
                }
                pendingTagName = part;
            }
        }
        if (pendingTagName != null) {
            current = getFirstChild(current, pendingTagName);
            if (current == null) throw new IllegalArgumentException("Тег не найден: " + pendingTagName);
        }
        return current.getTextContent().trim();
    }

    private Node getFirstChild(Node parent, String tag) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() == 1 && n.getNodeName().equals(tag)) return n;
        }
        return null;
    }

    private Node getNodeByIndex(Node parent, String tag, int index) {
        NodeList children = parent.getChildNodes();
        int count = 0;
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() == 1 && n.getNodeName().equals(tag)) {
                if (count == index) return n;
                count++;
            }
        }
        return null;
    }
}