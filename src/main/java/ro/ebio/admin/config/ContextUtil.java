package ro.ebio.admin.config;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Inject this instead of using FacesContext.something in JSF or
 * ApplicationContext.something in Spring to decouple the ro.ebio.admin.config.controller
 * from the actual framework.
 *
 * @author Catalin
 */
public interface ContextUtil {

    /**
     * Evaluates an EL expression of the form #{object.property}
     *
     * @param expression
     * @param clazz the evaluation class for the expression
     * @return
     */
    <T> T evaluateExpression(String expression, Class<T> clazz);

    /**
     * Retrieves the protocol, host, port and servlet context of this request If the port is 80 it is removed;
     *
     * The request object is retrieved from faces context
     *
     * @return this application's url
     */
    String getAppUrl();
    
    /**
     * Retrieves the protocol, host, port and servlet context of the page corresponding to the visitor's view
     * of this request. If the port is 80 it is removed;
     *
     * The request object is retrieved from faces context
     * @return 
     */
    default String getVisitorUrl() {
        return getAppUrl().replaceAll("-admin$", "");
    }

    /**
     * Sends a redirect to an external url
     *
     * @param externalUrl the url to redirect to
     * @throws IOException
     */
    void sendExternalRedirect(String externalUrl) throws IOException;

    /**
     * Sends a redirect to a local URL
     *
     * @param localUrl
     */
    void sendRedirect(String localUrl) throws IOException;

    /**
     * Reloads the current url to avoid losing the SEO-friendly url when submitting a form
     *
     * @throws IOException
     */
    void sendReload() throws IOException;

    /**
     * Invalidates the current HTTP session of this context.
     */
    void invalidateSession();

    /**
     * The HTTP request associated with this context.
     * @return
     */
    HttpServletRequest getRequest();

    /**
     *
     * @return the HTTP response associated with this context.
     */
    HttpServletResponse getResponse();

}
