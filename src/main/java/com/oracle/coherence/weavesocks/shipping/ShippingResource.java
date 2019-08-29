package com.oracle.coherence.weavesocks.shipping;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
    private NamedCache<String, Shipment> shipments;

    @GET
    @Produces(APPLICATION_JSON)
    public Collection<Shipment> getAllShipments() {
        return shipments.values();
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Shipment getShipmentById(@PathParam("id") String id) {
        return shipments.get(id);
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Shipment ship(Shipment shipment) {
        shipment.setTrackingNumber(UUID.randomUUID().toString());
        shipments.put(shipment.getId(), shipment);
        return shipment;
    }
}
