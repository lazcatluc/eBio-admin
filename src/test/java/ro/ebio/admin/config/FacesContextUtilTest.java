/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.ebio.admin.config;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Catalin
 */
public class FacesContextUtilTest {

    private FacesContextUtil facesContextUtil;
    private FacesContext facesContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private Application application;

    @Before
    public void setUp() {
        facesContext = mock(FacesContext.class);
        facesContextUtil = new FacesContextUtil(facesContext);
        request = mock(HttpServletRequest.class);
        ExternalContext ec = mock(ExternalContext.class);
        when(ec.getRequest()).thenReturn(request);
        response = mock(HttpServletResponse.class);
        when(ec.getResponse()).thenReturn(response);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(ec.getSession(anyBoolean())).thenReturn(session);
        application = mock(Application.class);
        when(facesContext.getApplication()).thenReturn(application);
        when(facesContext.getExternalContext()).thenReturn(ec);


        when(request.getRequestURL()).thenReturn(new StringBuffer("http://some-url.ro/some-uri/some-other-uri"));
    }

    @Test
    public void getAppUrlIsCookieUrl() {
        String expectedResult = "http://some-url.ro";
        when(request.getHeader("X-Original-Url")).thenReturn(expectedResult);

        String result = facesContextUtil.getAppUrl();

        assertEquals(expectedResult, result);
    }

    @Test
    public void getAppUriIsHeadOfRequestUrl() {
        String expectedResult = "http://some-url.ro/some-uri";

        String result = facesContextUtil.getAppUrl();

        assertEquals(expectedResult, result);
    }
    
    @Test
    public void getVisitorUriIsHeadOfRequestUrl() {
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://some-url.ro/some-uri-admin/some-other-uri"));
        String expectedResult = "http://some-url.ro/some-uri";

        String result = facesContextUtil.getVisitorUrl();

        assertEquals(expectedResult, result);
    }

    @Test
    public void sendRedirectWithoutInitialSlash() throws Exception {
        String redirectUri="second-uri";

        facesContextUtil.sendRedirect(redirectUri);

        verify(response, times(1)).sendRedirect("http://some-url.ro/some-uri/second-uri");
    }

    @Test
    public void sendRedirectWithInitialSlash() throws Exception {
        String redirectUri="/second-uri";

        facesContextUtil.sendRedirect(redirectUri);

        verify(response, times(1)).sendRedirect("http://some-url.ro/some-uri/second-uri");
    }

    @Test
    public void testSendReload() throws Exception {
        when(session.getAttribute("lastFancyUrl")).thenReturn("http://some-url.ro/some-uri/some-other-uri");
        facesContextUtil.sendReload();

        verify(response, times(1)).sendRedirect("http://some-url.ro/some-uri/some-other-uri");
    }

    @Test
    public void testSendExternalRedirect() throws Exception {
        facesContextUtil.sendExternalRedirect("http://some-external-url.ro/some-external-uri");

        verify(response, times(1)).sendRedirect("http://some-external-url.ro/some-external-uri");
    }

    @Test
    public void testEvaluateExpression() {
        when(application.evaluateExpressionGet(facesContext, "some-expression", String.class)).thenReturn("some-value");
        String expectedResult = "some-value";

        String result = facesContextUtil.evaluateExpression("some-expression", String.class);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetFacesContext() {
        facesContextUtil.getFacesContext();
    }

    @Test
    public void testInvalidateSession() {
        facesContextUtil.invalidateSession();

        verify(session, times(1)).invalidate();
    }

    @Test
    public void testGetRequest() {
        facesContextUtil.getRequest();
    }

    @Test
    public void testGetResponse() {
        facesContextUtil.getResponse();
    }

}
