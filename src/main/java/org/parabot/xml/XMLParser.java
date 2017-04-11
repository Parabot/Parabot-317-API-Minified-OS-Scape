package org.parabot.xml;

import org.ethan.oss.ServerEngine;
import org.ethan.oss.hook.AccessorHook;
import org.ethan.oss.hook.FieldHook;
import org.ethan.oss.hook.Hook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;

/**
 * @author JKetelaar
 */
public class XMLParser {
    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
        Hook.getInstance().init();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder        docBuilder = docFactory.newDocumentBuilder();

        Document doc         = docBuilder.newDocument();
        Element  rootElement = doc.createElement("injector");
        doc.appendChild(rootElement);

        Element interfaces = doc.createElement("interfaces");
        rootElement.appendChild(interfaces);

        for (AccessorHook classAccessor : Hook.getInstance().getAccessorHooks()) {
            Element add = doc.createElement("add");

            Element cn = doc.createElement("classname");
            cn.appendChild(doc.createTextNode(classAccessor.getClazz().replaceAll("\\.", "/")));

            Element i = doc.createElement("interface");
            i.appendChild(doc.createTextNode(classAccessor.getAccessor()));

            add.appendChild(cn);
            add.appendChild(i);

            interfaces.appendChild(add);
        }

        Element getters = doc.createElement("getters");
        rootElement.appendChild(getters);

        for (Map.Entry<String, FieldHook> fieldHookEntry : ServerEngine.getInstance().getFieldMap().entrySet()) {
            Element add = doc.createElement("add");

            String  accessor = getAccessor(fieldHookEntry.getValue().getClazz());
            Element a;
            if (accessor != null) {
                a = doc.createElement("accessor");
                a.appendChild(doc.createTextNode(accessor));
            } else {
                a = doc.createElement("classname");
                a.appendChild(doc.createTextNode(fieldHookEntry.getValue().getClazz().replaceAll("\\.", "/")));
            }

            Element f = doc.createElement("field");
            f.appendChild(doc.createTextNode(fieldHookEntry.getValue().getField()));

            Element m = doc.createElement("methodname");

            String field = fieldHookEntry.getKey();
            if (field.toLowerCase().startsWith("get")) {
                field = field.substring(3);
            }

            m.appendChild(doc.createTextNode("get" + field.substring(0, 1).toUpperCase() + field.substring(1)));

            if (!fieldHookEntry.getValue().getClazz().equals(fieldHookEntry.getValue().getInto())) {
                Element i = doc.createElement("into");
                i.appendChild(doc.createTextNode(fieldHookEntry.getValue().getInto().replaceAll("\\.", "/")));

                add.appendChild(i);
            }

            add.appendChild(a);
            add.appendChild(f);
            add.appendChild(m);

            getters.appendChild(add);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer        transformer        = transformerFactory.newTransformer();
        DOMSource          source             = new DOMSource(doc);
        StreamResult       result             = new StreamResult(new File("/Users/jeroen/Desktop/file.xml"));

        transformer.transform(source, result);
    }

    private static String getAccessor(String classname) {
        for (AccessorHook classAccessor : Hook.getInstance().getAccessorHooks()) {
            if (classAccessor.getClazz().equals(classname)) {
                return classAccessor.getAccessor();
            }
        }
        return null;
    }
}
