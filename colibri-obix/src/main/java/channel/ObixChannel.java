package channel;

import model.ObixLobby;
import model.ObixObject;
import obix.Obj;
import org.eclipse.californium.core.coap.CoAP;

import java.util.*;

import static org.eclipse.californium.core.coap.MediaTypeRegistry.APPLICATION_XML;

/**
 * This Interface is implemented by plain channels, for example the {@link CoapChannel}.
 * Each plain channel has to be decorated by at least one channel decorator, for example the {@link ObixXmlChannelDecorator}.
 */
public abstract class ObixChannel {

    protected String baseUri;
    protected String lobbyUri;
    protected List<String> observedTypes;
    protected Map<String, ObixObject> observedObjects = new HashMap<String, ObixObject>();
    protected Integer port;

    public ObixChannel() {
    }

    public ObixChannel(String baseUri, String lobbyUri, List<String> observedTypes) {
        this.baseUri = baseUri;
        this.lobbyUri = lobbyUri;
        this.observedTypes = observedTypes;
    }

    public ObixChannel(String baseUri, String lobbyUri, Integer port, List<String> observedTypes) {
        this.baseUri = baseUri;
        this.lobbyUri = lobbyUri;
        this.port = port;
        this.observedTypes = observedTypes;
    }

    /**
     * Requests the oBIX lobby with the lobby URI of the channel.
     * Uses XML as messgae format.
     *
     * @return                  The oBIX lobby of the channel.
     */
    public ObixLobby getLobby() {
        return this.getLobby(lobbyUri, APPLICATION_XML);
    }

    /**
     * Requests the oBIX lobby specified URI.
     * Uses XML as messgae format.
     *
     * @param   uri             The URI of the requested oBIX lobby.
     * @return                  The oBIX lobby of the specified URI.
     */
    public ObixLobby getLobby(String uri) {
        return this.getLobby(uri, APPLICATION_XML);
    }

    /**
     * Observers data from the oBIX resource mapped to the oBIXObject.
     *
     * NOTE: ONLY XML SUPPORTED SO FAR.
     *
     * @param   obj             The oBIXObject which is observed.
     * @return                  The observed oBIX Object with.
     */
    public ObixObject observe(ObixObject obj) {
        return this.observe(obj, APPLICATION_XML);
    }

    /**
     * Requests data from the oBIX resource with the specified URI.
     *
     * NOTE: ONLY XML SUPPORTED SO FAR.
     *
     * @param   uri             The URI of the requested oBIX resource.
     * @return                  The oBIX Object with the specified URI.
     */
    public ObixObject get(String uri) {
        return this.get(uri, APPLICATION_XML);
    }

    /**
     * Modify an oBIX object with a put call.
     *
     * NOTE: ONLY XML SUPPORTED SO FAR.
     *
     * @param   obj             The oBIX object which is modified.
     * @return                  The modified oBIX Object.
     */
    public ObixObject put(ObixObject obj) {
        return this.put(obj, APPLICATION_XML);
    }

    /**
     * Modify an oBIX object with a put call.
     *
     * NOTE: ONLY XML SUPPORTED SO FAR.
     *
     * @param   obj             The oBIX object which is modified.
     * @param   mediaType       The requested media type, for example APPLICATION_XML.
     * @return                  The modified oBIX Object.
     */
    public abstract ObixObject put(ObixObject obj, int mediaType);

    /**
     * Requests the oBIX lobby specified URI.
     * Uses XML as messgae format.
     *
     * @param   uri             The URI of the requested oBIX lobby.
     * @param   mediaType       The requested media type, for example APPLICATION_XML.
     * @return                  The oBIX lobby of the specified URI.
     */
    public abstract ObixLobby getLobby(String uri, int mediaType);

    /**
     * Requests data from the oBIX resource with the specified URI.
     *
     * NOTE: ONLY XML SUPPORTED SO FAR.
     *
     * @param   uri             The URI of the requested oBIX resource.
     * @param   mediaType       The requested media type, for example APPLICATION_XML.
     * @return                  The oBIX Objects with the specified URI.
     */
     public abstract ObixObject get(String uri, int mediaType);

    /**
     * Observers data from the oBIX resource mapped to the oBIXObject.
     *
     * NOTE: ONLY XML SUPPORTED SO FAR.
     *
     * @param   obj             The oBIXObject which is observed.
     * @param   mediaType       The requested media type, for example APPLICATION_XML.
     * @return                  The observed oBIX Object with.
     */
    public abstract ObixObject observe(ObixObject obj, int mediaType);

    /**
     * Normalizes the given URI against the CoAP-base URI of the channel
     *
     * @param uri   The URI which should be normalized
     * @return      The normalized URI
     */
    public abstract String normalizeUri(String uri);

    public static String normalizeUri(String uri, String baseUri) {
        if(uri.contains(":")) {
            uri = uri.split(":")[1];
        }
        if(uri.contains("%") || baseUri.contains("%")) {
            System.out.println("Please do not use % in URIs when using CoAP. This URI is skipped.");
            uri = uri.replace("%", "");
            baseUri = baseUri.replace("%", "");
        }
        String[] uriPaths = uri.split("/");
        String newUri = baseUri;
        List<String> notUsedPaths = new ArrayList<String>(Arrays.asList(uriPaths));
        for(String s : uriPaths) {
            if(newUri.contains(s)) {
                notUsedPaths.remove(s);
                break;
            }
        }
        for(String s: notUsedPaths) {
            newUri += "/" + s;
        }
        return newUri;
    }

    /**
     * Returns the base URI of the oBIX Channel
     *
     * @return  The base URI of the oBIX channel.
     */
    public String getBaseUri() {
        return this.baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getLobbyUri() {
        return lobbyUri;
    }

    public void setLobbyUri(String lobbyUri) {
        this.lobbyUri = lobbyUri;
    }

    public List<String> getObservedTypes() {
        return observedTypes;
    }

    public void setObservedTypes(List<String> observedTypes) {
        this.observedTypes = observedTypes;
    }

    public Map<String, ObixObject> getObservedObjects() {
        return observedObjects;
    }

    public Integer getPort() {
        return port;
    }
}
