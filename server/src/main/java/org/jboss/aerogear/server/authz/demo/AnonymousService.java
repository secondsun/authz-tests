/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.server.authz.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.jboss.aerogear.server.authz.demo.beans.StringsBean;

/**
 *
 * @author summers
 */
@ApplicationScoped
@Path("/string")
@Api(value = "/string", tags = "string")
public class AnonymousService {
    @Inject
    private StringsBean stringsBean;
    
    @GET
    @Produces("application/json")
    @ApiOperation(value = "Gets the Strings stored in the demo",
            response = String.class
    )
    public String getStrings() {
        return stringsBean.getStrings();
    }
    
    
}
