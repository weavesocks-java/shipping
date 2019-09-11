package com.oracle.coherence.weavesocks.shipping;

import java.util.Collection;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.helidon.microprofile.grpc.core.Unary;

import com.tangosol.net.NamedCache;

@ApplicationScoped
public class ShippingService {

    @Inject
    private NamedCache<String, Shipment> shipments;

    @Unary
    public Collection<Shipment> getAllShipments(String id) {
        return shipments.values();
    }

    @Unary
    public Shipment getShipmentById(String id) {
        return shipments.get(id);
    }

    @Unary
    public Shipment ship(Shipment shipment) {
        shipment.setTrackingNumber(UUID.randomUUID().toString());
        shipments.put(shipment.getId(), shipment);

        return shipment;
    }
}
