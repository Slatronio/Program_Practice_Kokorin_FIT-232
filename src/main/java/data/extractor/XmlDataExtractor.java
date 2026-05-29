package data.extractor;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class XmlDataExtractor implements DataExtractor {

    @Override
    public String extract(String resourcePath, String fieldPath) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new IllegalArgumentException("Файл не найден в resources: " + resourcePath);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setNamespaceAware(false);
            Document doc = dbf.newDocumentBuilder().parse(is);

            Node current = doc.getDocumentElement(); // <root>
            String[] parts = fieldPath.split("/");
            String pendingTagName = null;

            for (String part : parts) {
                if (part.isEmpty()) continue;

                if (part.startsWith("[") && part.endsWith("]")) {
                    int index = Integer.parseInt(part.substring(1, part.length() - 1));
                    if (pendingTagName == null) {
                        throw new IllegalArgumentException("Индекс без имени тега: " + part);
                    }
                    current = getNodeByTagNameAndIndex(current, pendingTagName, index);
                    if (current == null) {
                        throw new IllegalArgumentException("Элемент не найден: " + pendingTagName + "[" + index + "]");
                    }
                    pendingTagName = null; // Сбрасываем после выбора по индексу
                } else {
                    // Если был предыдущий тег без индекса, берём его первый элемент
                    if (pendingTagName != null) {
                        current = getFirstChildByTagName(current, pendingTagName);
                        if (current == null) throw new IllegalArgumentException("Тег не найден: " + pendingTagName);
                    }
                    pendingTagName = part; // Запоминаем текущий тег для следующего шага
                }
            }

            // Обработка последнего тега в пути
            if (pendingTagName != null) {
                current = getFirstChildByTagName(current, pendingTagName);
                if (current == null) throw new IllegalArgumentException("Тег не найден: " + pendingTagName);
            }

            return current.getTextContent().trim();
        }
    }

    private Node getFirstChildByTagName(Node parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(tagName)) {
                return child;
            }
        }
        return null;
    }

    private Node getNodeByTagNameAndIndex(Node parent, String tagName, int index) {
        NodeList children = parent.getChildNodes();
        int count = 0;
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(tagName)) {
                if (count == index) return child;
                count++;
            }
        }
        return null;
    }
}