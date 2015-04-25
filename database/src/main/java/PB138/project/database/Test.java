package PB138.project.database;

import org.exist.xmldb.EXistResource;
import org.exist.xmldb.XQueryService;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

/**
 *
 * @author bar
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        Collection col = null;
        try {
            String driver = "org.exist.xmldb.DatabaseImpl";
            String prefix = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";

            Class c = Class.forName(driver);
            Database database = (Database) c.newInstance();
            //database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            //col = DatabaseManager.getCollection(prefix + "school");
            col = DatabaseManager.getCollection(prefix + "cars", "admin", "test123");
            /*String xpath = "<products_labour_costs>" +
                    "{" +
                    "    let $a := doc(\"/db/cars/asd\")" +
                    "    for $division in $a//division" +
                    "        return <division id=\"{$division/@id}\">" +
                    "                {" +
                    "                for $product in $division/products/product" +
                    "                return <product name=\"{$product/name}\" costs=\"{" +
                    "                    sum(" +
                    "                        for $employee in $product/producers/employee" +
                    "                        return $a/company/employee[@id=$employee/@id]/@payment" +
                    "                    )" +
                    "                    }\"/>" +
                    "                }" +
                    "                </division>" +
                    "}" +
                    "</products_labour_costs>";*/
            String xpath = "let $doc := doc($document)" +
                    "return " +
                    "update insert <car>asd</car> into $doc/cars";

            XQueryService service = (XQueryService) col.getService("XQueryService", "1.0");
            service.declareVariable("document", "/db/cars/cars.xml");
            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xpath);

            ResourceSet resultSet = service.execute(compiled);
            /*ResourceIterator results = resultSet.getIterator();
            Resource res = null;
            while(results.hasMoreResources()) {
                res = results.nextResource();
                System.out.println(res.getContent());
            }*/



            /*String doc = "<xml>Hello World!</xml>";
            String id = "asd";
            // Create a new XML resource with the specified ID
            XMLResource res = (XMLResource) col.createResource(id,
                    XMLResource.RESOURCE_TYPE);


            // Set the content of the XML resource as the document
            res.setContent(doc);

            // Store the resource into the database
            col.storeResource(res);
                */
        } catch (XMLDBException e) {
            System.err.println("XML:DB Exception occurred " + e.errorCode + " " + e.getMessage());
        } finally {
            if (col != null) { col.close(); }
        }
    }

}

