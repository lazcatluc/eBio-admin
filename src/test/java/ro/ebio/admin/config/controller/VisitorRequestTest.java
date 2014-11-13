/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.ebio.admin.config.controller;

import javax.servlet.http.HttpServletRequest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ro.ebio.admin.config.ContextUtil;

/**
 *
 * @author Catalin
 */
public class VisitorRequestTest {
    
    @Test
    public void visitorRequestForgetsAdmin() throws Exception {
        VisitorRequest visitorRequest = new VisitorRequest();
        ContextUtil contextUtil = mock(ContextUtil.class);
        visitorRequest.setContextUtil(contextUtil);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(contextUtil.getRequest()).thenReturn(request);
        when(contextUtil.getVisitorUrl()).thenReturn("http://some-url.ro/some-uri");
        when(contextUtil.getAppUrl()).thenReturn("http://some-url.ro/some-uri-admin");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://some-url.ro/some-uri-admin/some-context"));
        
        assertEquals("http://some-url.ro/some-uri/some-context", visitorRequest.getVisitUrl());
    }
    
}
