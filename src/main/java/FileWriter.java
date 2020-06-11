import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileWriter {

    private static final String PRODUCTS_TAG = "products";
    private static final String PRODUCT_TAG = "product";
    private static final String DESCRIPTION_TAG = "description";
    private static final String GTIN_TAG = "gtin";
    private static final String PRICE_TAG = "price";
    private static final String ORDERID_TAG = "orderid";


    /*
    Function write an XML file
    Input: output folder
           suffix of the filename
           supplier with products
    */
    public void writeXMLFile(String outputdir, String orderno, Supplier supplier){
        //create filename form folder + supplier name + orderno(suffix) + extension
        String filename = outputdir.toString() + supplier.getSupplierName().toLowerCase()+ orderno + ".xml";
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

        try {
            XMLEventWriter xmlEventWriter = xmlOutputFactory.createXMLEventWriter(new FileOutputStream(filename), "UTF-8");
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n"); //new line
            XMLEvent tab = eventFactory.createDTD("\t"); //indent tag

            xmlEventWriter.add(eventFactory.createStartDocument());
            xmlEventWriter.add(end);
            xmlEventWriter.add(eventFactory.createStartElement("","", PRODUCTS_TAG));
            xmlEventWriter.add(end);

            // Write the element nodes
            for (Product product : supplier.getProductList()) {
                xmlEventWriter.add(tab);
                xmlEventWriter.add(eventFactory.createStartElement("", "", PRODUCT_TAG));
                xmlEventWriter.add(end);

                try {
                    createNode(xmlEventWriter, DESCRIPTION_TAG, product.getDescription(), null);
                    createNode(xmlEventWriter, GTIN_TAG, product.getGtin(), null);

                    Map<String, String> attributes =  new HashMap<String, String>() {{
                        put("currency",product.getCurrency());
                    }};
                    createNode(xmlEventWriter, PRICE_TAG, Double.toString(product.getPrice()), attributes);
                    createNode(xmlEventWriter, ORDERID_TAG, product.getOrderid(), null);

                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }

                xmlEventWriter.add(tab);
                xmlEventWriter.add(eventFactory.createEndElement("", "", PRODUCT_TAG));
                xmlEventWriter.add(end);

            }

            xmlEventWriter.add(eventFactory.createEndElement("", "", PRODUCTS_TAG));
            xmlEventWriter.add(end);
            xmlEventWriter.add(eventFactory.createEndDocument());

            xmlEventWriter.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createNode(XMLEventWriter eventWriter, String element, String value, Map<String, String> attributes) throws XMLStreamException {
        XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();
        XMLEvent end = xmlEventFactory.createDTD("\n"); //new line
        XMLEvent tab = xmlEventFactory.createDTD("\t\t"); //indent tag
        //Create Start node
        eventWriter.add(tab);
        eventWriter.add(xmlEventFactory.createStartElement("", "", element));
        if (attributes != null ){
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                eventWriter.add(xmlEventFactory.createAttribute(entry.getKey(), entry.getValue()));
            }
        }
        //Create Content
        eventWriter.add(xmlEventFactory.createCharacters(value));
        // Create End node
        eventWriter.add(xmlEventFactory.createEndElement("", "", element));
        eventWriter.add(end);
    }
}
