
package org.jboss.as.quickstarts.html5rest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/")
public class HelloWorld {
    @Inject
    HelloService helloService;

    @POST
    @Path("/json/{name}")
    @Produces("application/json")
    public String getHelloWorldJSON(@PathParam("name") String name) {
        System.out.println("name: " + name);
        return "{\"result\":\"" + helloService.createHelloMessage(name) + "\"}";
    }

    @POST
    @Path("/xml/{name}")
    @Produces("application/xml")
    public String getHelloWorldXML(@PathParam("name") String name) {
        System.out.println("name: " + name);
        return "<xml><result>" + helloService.createHelloMessage(name) + "</result></xml>";
    }

}
