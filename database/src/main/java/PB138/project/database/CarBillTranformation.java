package PB138.project.database;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * Created by Michal on 15.5.2015.
 */
public class CarBillTranformation {

    public static void makeBill()
            throws TransformerConfigurationException, TransformerException {

        TransformerFactory tf = TransformerFactory.newInstance();

        //System.out.println(tf.getClass());

        Transformer xsltProc = tf.newTransformer(
                new StreamSource(new File("src/main/resources/xslt/bill_of_sale.xsl")));

        xsltProc.transform(
                new StreamSource(new File("src/main/resources/carForSale.xml")),
                new StreamResult(new File("src/main/resources/carBill.html")));
    }
}
