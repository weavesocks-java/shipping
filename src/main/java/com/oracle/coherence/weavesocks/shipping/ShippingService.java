package com.oracle.coherence.weavesocks.shipping;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import io.helidon.grpc.core.MarshallerSupplier;
import io.helidon.microprofile.grpc.core.GrpcMarshaller;
import io.helidon.microprofile.grpc.core.RpcService;
import io.helidon.microprofile.grpc.core.ServerStreaming;
import io.helidon.microprofile.grpc.core.Unary;

import com.oracle.coherence.helidon.io.PofMarshaller;
import com.oracle.io.pof.PortableTypeSerializer;
import com.oracle.io.pof.SimplePofContext;
import com.tangosol.net.NamedCache;
import io.grpc.MethodDescriptor;

@RpcService
@GrpcMarshaller("shipping")
@ApplicationScoped
public class ShippingService {
    private static final Logger LOGGER = Logger.getLogger(ShippingService.class.getName());

    @Inject
    private NamedCache<String, Shipment> shipments;

    @ServerStreaming
    public Stream<Shipment> getAllShipments(String id) {
        return shipments.values().stream();
    }

    @Unary
    public Shipment getShipmentById(String id) {
        return shipments.get(id);
    }

    @Unary
    public Shipment ship(Shipment shipment) {
        LOGGER.log(Level.INFO, "Processing shipment: " + shipment);
        shipment.setTrackingNumber(createTrackingNumber());
        shipments.put(shipment.getId(), shipment);
        LOGGER.log(Level.INFO, "Processed shipment: " + shipment);

        return shipment;
    }

    private String createTrackingNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // ---- inner class: Marshaller -----------------------------------------

    @ApplicationScoped
    @Named("shipping")
    public static class Marshaller implements MarshallerSupplier {

        private final MethodDescriptor.Marshaller<?> marshaller;

        public Marshaller() {
            SimplePofContext ctx = new SimplePofContext();
            ctx.registerUserType(1, Shipment.class, new PortableTypeSerializer(1, Shipment.class));

            marshaller = new PofMarshaller(ctx);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> MethodDescriptor.Marshaller<T> get(Class<T> aClass) {
            return (MethodDescriptor.Marshaller<T>) marshaller;
        }
    }
}
