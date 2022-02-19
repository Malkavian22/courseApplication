package com.example.javafxhelpapllication;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import javafx.scene.control.ProgressBar;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLReader {

    public static final String COURSES = "//courses/course";
    public static final String THEMES = "//courses/course/themes/theme";
    public static final String FILE = "D:\\File2\\test.xml";

    public static NodeList getTags(String path, String file) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression xPathExpression = xpath.compile(path);

        return (NodeList) xPathExpression.
                evaluate(document, XPathConstants.NODESET); //вернет список тегов
    }

    /**
        Вернет строку для получения тега <themes></> по фильтру
     */
    public static String getAllThemesCourse(String filter){
        return "//course" + filter + "/themes";
    }
    /**
     * @param filter фильтр для тэга <themes>
     * @return путь в xml на все подтемы текущей темы по фильтру
     */
    public static String getAllTheme(String filter){
        return "//themes" + filter + "/theme";
    }

    public void updateProgressNode(ProgressBar progress_theme, String theme) throws TransformerException {

        double progress = progress_theme.getProgress();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(FILE));
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("theme");

            for (int i = 0; i < nodes.getLength(); i++) {

                Node node = nodes.item(i);
                if(!node.getAttributes().getNamedItem("name").getTextContent().equals(theme)){
                    continue;
                }
                Node attr = node.getAttributes().getNamedItem("progress");

                double old = Double.parseDouble(attr.getTextContent());

                if (old < progress) {
                    attr.setNodeValue(String.valueOf(progress));
                    saveDoc(doc);
                }
            }
        } catch (Exception e){
        e.printStackTrace();
    }
    }

    private void saveDoc(Document doc) throws TransformerException {
        doc.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(FILE));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }

}
