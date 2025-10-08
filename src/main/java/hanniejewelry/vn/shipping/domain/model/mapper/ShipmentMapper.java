package hanniejewelry.vn.shipping.domain.model.mapper;

import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.shipping.api.dto.ShipmentRequest;
import hanniejewelry.vn.shipping.domain.model.entity.Shipment;
import hanniejewelry.vn.shipping.domain.model.entity.ShipmentItem;
import hanniejewelry.vn.shipping.domain.model.enums.CODStatus;
import hanniejewelry.vn.shipping.domain.model.enums.ShipmentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Component
public class ShipmentMapper {
    
    public Shipment toEntity(ShipmentRequest request, Order order) {
        if (request == null || order == null) {
            return null;
        }

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setCarrierCode(request.getCarrierCode());
        shipment.setCarrierName(request.getCarrierName());
        shipment.setTrackingNumber(request.getTrackingNumber());
        shipment.setTrackingUrl(request.getTrackingUrl());
        shipment.setNotes(request.getNotes());
        shipment.setStatus(ShipmentStatus.PICKING_UP);
        shipment.setCodStatus(CODStatus.NOT_APPLICABLE);
        shipment.setCodAmount(order.getTotalPrice() != null ? order.getTotalPrice() : BigDecimal.ZERO);
        shipment.setPrintCount(0);
        shipment.setCreatedAt(Instant.now());
        shipment.setUpdatedAt(Instant.now());
        
        return shipment;
    }

    public ShipmentItem toShipmentItem(Long variantId, UUID orderLineItemId, UUID productId, Integer quantity) {
        ShipmentItem item = new ShipmentItem();
        item.setVariantId(variantId);
        item.setOrderLineItemId(orderLineItemId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }

    public void updateShipmentStatus(Shipment shipment, ShipmentStatus status) {
        if (shipment == null || status == null) {
            return;
        }
        shipment.setStatus(status);
        shipment.setUpdatedAt(Instant.now());
    }

    public void updateCODStatus(Shipment shipment, CODStatus status, BigDecimal amount) {
        if (shipment == null || status == null) {
            return;
        }
        shipment.setCodStatus(status);
        if (amount != null) {
            shipment.setCodAmount(amount);
        }
        shipment.setUpdatedAt(Instant.now());
    }
} 