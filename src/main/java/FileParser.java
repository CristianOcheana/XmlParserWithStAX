import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileParser {

    private static final String ORDER_TAG = "order";
    private static final String PRODUCT_TAG = "product";
    private static final String DESCRIPTION_TAG = "description";
    private static final String GTIN_TAG = "gtin";
    private static final String PRICE_TAG = "price";
    private static final String SUPPLIER_TAG = "supplier";


    /*
    Function process an XML file
    Input: path to file
    Output: List of suppliers and their products
     */
    public Collection<Supplier> processFile(String filename) {
        //create list of suppliers
        Map<String, Supplier> supplierList = new HashMap<String, Supplier>();


        String text = null;
        Product currentProduct = null;
        Supplier currentSupplier = null;
        Date timestamp = null;
        String currentOrderid = null;
        String currentSupplierName = null;
        String currentCurrency = null;

        //parse xml file using StAX
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filename));
            while (reader.hasNext()) {
                int Event = reader.next();
                switch (Event) {
                    case XMLStreamConstants.START_ELEMENT: {
                        switch (reader.getLocalName()){
                            case ORDER_TAG: {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                try {
                                    timestamp = simpleDateFormat.parse(reader.getAttributeValue(null, "created"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                currentOrderid = reader.getAttributeValue(null, "ID");
                                break;
                            }
                            case PRODUCT_TAG: {
                                currentProduct = new Product(currentOrderid, timestamp);
                                break;
                            }
                            case PRICE_TAG: {
                                currentCurrency = reader.getAttributeValue(null, "currency");
                                break;
                            }
                        }
                        break;
                    }
                    case XMLStreamConstants.CHARACTERS: {
                        text = reader.getText().trim();
                        break;
                    }
                    case XMLStreamConstants.END_ELEMENT: {
                        switch (reader.getLocalName()){
                            case DESCRIPTION_TAG: {
                                currentProduct.setDescription(text);
                                break;
                            }
                            case GTIN_TAG: {
                                currentProduct.setGtin(text);
                                break;
                            }
                            case PRICE_TAG: {
                                currentProduct.setPrice(Double.parseDouble(text));
                                break;
                            }
                            case SUPPLIER_TAG: {
                                currentSupplierName = text;
                                if (!supplierList.containsKey(currentSupplierName) ) {
                                    currentSupplier = new Supplier(currentSupplierName);
                                    supplierList.put(currentSupplierName, currentSupplier);
                                }
                                break;
                            }
                            case PRODUCT_TAG: {
                                currentProduct.setOrderid(currentOrderid);
                                currentProduct.setCurrency(currentCurrency);
                                supplierList.get(currentSupplierName).addProduct(currentProduct);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return supplierList.values();
    }
}
