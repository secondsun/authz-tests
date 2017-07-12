/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.server.authz.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.jboss.aerogear.server.authz.demo.beans.StringsBean;

/**
 *
 * @author summers
 */
@ApplicationScoped
@Path("/secure/string")
@Api(value = "/secure/string", tags = "string")
@RolesAllowed("user")
public class SecuredService {
    
    @Inject
    private StringsBean stringsBean;
    
    @GET
    @Produces("application/json")
    @ApiOperation(value = "Gets the Strings stored in the demo",
            response = String.class
    )
    public  String getStrings() {
        return stringsBean.getStrings();
    }
    
    @PUT
    @POST
    @Produces("application/json")
    @ApiOperation(value = "Adds a String and returns the new set of Strings",
            response = String.class
    )
    public  String addString(String stringToAdd) {
        stringsBean.addString(stringToAdd);
        return  getStrings();
    }
    
    @DELETE
    @Produces("application/json")
    @ApiOperation(value = "Removes a String and returns the new set of Strings",
            response = String.class
    )
    public  String removeString(String stringToRemove) {
        stringsBean.removeString(stringToRemove);
        return  getStrings();
    }
    
}
