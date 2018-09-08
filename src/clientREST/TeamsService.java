
package clientREST;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "TeamsService", targetNamespace = "http://sunnyREST/", wsdlLocation = "http://localhost:8085/Web-service?wsdl")
public class TeamsService
    extends Service
{

    private final static URL TEAMSSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(clientREST.TeamsService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = clientREST.TeamsService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8085/Web-service?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8085/Web-service?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        TEAMSSERVICE_WSDL_LOCATION = url;
    }

    public TeamsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TeamsService() {
        super(TEAMSSERVICE_WSDL_LOCATION, new QName("http://sunnyREST/", "TeamsService"));
    }

    /**
     * 
     * @return
     *     returns Teams
     */
    @WebEndpoint(name = "TeamsPort")
    public Teams getTeamsPort() {
        return super.getPort(new QName("http://sunnyREST/", "TeamsPort"), Teams.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Teams
     */
    @WebEndpoint(name = "TeamsPort")
    public Teams getTeamsPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://sunnyREST/", "TeamsPort"), Teams.class, features);
    }

}