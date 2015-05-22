package PB138.project.database;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.net.URL;

/**
 * Created by Michal on 15.5.2015.
 */
public class CarBillTranformation {

    public static void makeBill(String path)
            throws  TransformerException {

        TransformerFactory tf = TransformerFactory.newInstance();

        //System.out.println(tf.getClass());
        Transformer xsltProc = tf.newTransformer(
                new StreamSource(new File(path+"WEB-INF\\classes\\xslt\\bill_of_sale.xsl")));

        xsltProc.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        xsltProc.transform(
                new StreamSource(new File(path+"WEB-INF\\classes\\tmp\\contract.xml")),
                new StreamResult(new File(path+"car.html")));
    }
}
