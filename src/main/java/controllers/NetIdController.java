package controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class NetIdController {

    @GET
    public String checkWorking() {
        return "Working - True";
    }

    @Path("netid/")
    @GET
    public String getNetId() {
        return "rt443";
    }
}