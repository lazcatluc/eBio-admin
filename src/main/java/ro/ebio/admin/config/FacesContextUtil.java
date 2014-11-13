/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.ebio.admin.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Catalin
 */
public class FacesContextUtil implements ContextUtil, Serializable {

    private static final long serialVersionUID = 1l;

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(FacesContextUtil.class.getName());

    private final transient FacesContext facesContext;

    public FacesContextUtil() {
        facesContext = null;
    }

    public FacesContextUtil(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    private static String getUrlFromRequest(HttpServletRequest request) {
        String bigIPHeader = request.getHeader("X-Original-Url");
        if (bigIPHeader == null) {
            return request.getRequestURL().toString();
        } else {
            return bigIPHeader;
        }
    }

    private static final int MAX_COUNT_SLASHES = 4;

    private static String getAppUrlFromRequest(HttpServletRequest request) {
        String[] spl = getUrlFromRequest(request).split("/");
        StringBuilder ret = new StringBuilder();
        int count = 0;
        while (count < MAX_COUNT_SLASHES && count < spl.length) {
            if (count > 0) {
                ret.append("/");
            }
            ret.append(spl[count]);
            count++;
        }
        return ret.toString();
    }

    /**
     * Retrieves the protocol, host, port and servlet context of this request If the port is 80 it is removed;
     *
     * The request object is retrieved from faces context
     *
     * @return this application's url
     */
    @Override
    public String getAppUrl() {
        return getAppUrlFromRequest(getRequest());
    }

    /**
     * Sends a redirect to a local URL
     *
     * @param localUrl
     */
    @Override
    public void sendRedirect(String localUrl) throws IOException {
        sendExternalRedirect(getAppUrl()
                + (localUrl.startsWith("/") ? "" : "/") + localUrl);
    }

    /**
     * Reloads the current url to avoid losing the SEO-friendly url when submitting a form
     *
     * @throws IOException
     */
    @Override
    public void sendReload() throws IOException {
        HttpServletRequest request = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
        String redirect = (String) request.getSession().getAttribute("lastFancyUrl");
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Sending redirect to {0}", new Object[]{redirect});
        }
        sendExternalRedirect(redirect);
    }

    /**
     * Sends a redirect to an external url
     *
     * @param externalUrl the url to redirect to
     * @throws IOException
     */
    @Override
    public void sendExternalRedirect(String externalUrl) throws IOException {
        try {
            HttpServletRequest request = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
            ((HttpServletResponse) getFacesContext().getExternalContext().getResponse()).sendRedirect(
                    externalUrl == null ? getAppUrlFromRequest(request) : externalUrl);
            getFacesContext().responseComplete();
        } catch (IllegalStateException ise) {
            //already redirected
        }
    }

    /**
     * Evaluates an EL expression of the form #{object.property}
     *
     * @param expression
     * @param clazz the evaluation class for the expression
     * @return
     */
    @Override
    public <T> T evaluateExpression(String expression, Class<T> clazz) {
        return getFacesContext().getApplication().evaluateExpressionGet(
                getFacesContext(), expression, clazz);
    }

    /**
     * @return the facesContext
     */
    public FacesContext getFacesContext() {
        return facesContext == null ? FacesContext.getCurrentInstance() : facesContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidateSession() {
        ((HttpSession) getFacesContext().getExternalContext().getSession(true)).invalidate();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public HttpServletRequest getRequest() {
        return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public HttpServletResponse getResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }
}
