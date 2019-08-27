package com.oracle.coherence.weavesocks.shipping;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.tangosol.net.NamedCache;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@ApplicationScoped
@Path("/shipping")
public class ShippingResource {

    @Inject
    private NamedCache<String, Shipment> shipping;

    @GET
    public String getShipping() {
        return "GET ALL" + shipping.size() + " Shipping Resource.";
    }

    @GET
    @Path("{id}")
    public String getShippingById(@PathParam("id") String id) {
        Shipment shipment = shipping.get(id);
        return "GET Shipping Resource with id: " + id;
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Shipment postShipping(Shipment shipment) {
        shipping.put(shipment.getId(), shipment);
        return shipment;
    }
}
