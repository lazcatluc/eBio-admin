package ro.ebio.admin.config.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import ro.ebio.admin.config.ContextUtil;

/**
 * Utility bean for providing preview urls to
 * regular pages from their admin corresponding pages.
 * 
 * @author Catalin
 */
@ManagedBean(name="visitor")
@RequestScoped
public class VisitorRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private ContextUtil contextUtil;
    
    public String getVisitUrl() {
        return contextUtil.getVisitorUrl() + 
                contextUtil.getRequest().getContextPath();
    }

    /**
     * @return the contextUtil
     */
    public ContextUtil getContextUtil() {
        return contextUtil;
    }

    /**
     * @param contextUtil the contextUtil to set
     */
    public void setContextUtil(ContextUtil contextUtil) {
        this.contextUtil = contextUtil;
    }
}
