package hanniejewelry.vn.shipping.api;

import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shipping.api.dto.FulfillmentRequest;
import hanniejewelry.vn.shipping.api.dto.ShipmentRequest;
import hanniejewelry.vn.shipping.api.dto.UpdateCODStatusRequest;
import hanniejewelry.vn.shipping.api.dto.UpdateShipmentStatusRequest;
import hanniejewelry.vn.shipping.domain.model.view.ShipmentView;
import hanniejewelry.vn.shipping.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShipmentAPIController {
    
    private final ShipmentService shipmentService;
    
    @GetMapping(ApiConstants.API_PREFIX + "/shipments")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<RestResponse<Map<String, Object>>> getShipmentsByFilter(
            @ModelAttribute GenericFilterRequest filter) {
        Page<ShipmentView> shipments = shipmentService.getAllShipmentsWithFilter(filter);
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> pagedData = new HashMap<>();
        
        pagedData.put("content", shipments.getContent());
        pagedData.put("totalElements", shipments.getTotalElements());
        pagedData.put("totalPages", shipments.getTotalPages());
        pagedData.put("number", shipments.getNumber());
        pagedData.put("size", shipments.getSize());
        
        result.put("result", pagedData);
        
        return RestResponseUtils.successResponse(result);
    }
    
    @GetMapping(ApiConstants.API_PREFIX + "/shipments/{id}")
    public ResponseEntity<RestResponse<ShipmentView>> getShipmentById(@PathVariable UUID id) {
        ShipmentView shipment = shipmentService.getShipmentViewById(id);
        return RestResponseUtils.successResponse(shipment);
    }
    
    @PostMapping(ApiConstants.API_PREFIX + "/shipments")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<RestResponse<ShipmentView>> createShipment(@Valid @RequestBody ShipmentRequest request) {
        log.info("Creating shipment for order: {}", request.getOrderId());
        ShipmentView shipment = shipmentService.createShipmentAndReturnView(request);
        return RestResponseUtils.createdResponse(shipment);
    }
    
    @PutMapping(ApiConstants.API_PREFIX + "/shipments/{id}/status")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<RestResponse<ShipmentView>> updateShipmentStatus(
            @PathVariable UUID id, @Valid @RequestBody UpdateShipmentStatusRequest request) {
        log.info("Updating status for shipment: {}, new status: {}", id, request.getStatus());
        ShipmentView shipment = shipmentService.updateShipmentStatus(id, request);
        return RestResponseUtils.successResponse(shipment);
    }
    
    @PutMapping(ApiConstants.API_PREFIX + "/shipments/{id}/cod")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<RestResponse<ShipmentView>> updateCODStatus(
            @PathVariable UUID id, @Valid @RequestBody UpdateCODStatusRequest request) {
        log.info("Updating COD status for shipment: {}, new status: {}", id, request.getStatus());
        ShipmentView shipment = shipmentService.updateCODStatus(id, request);
        return RestResponseUtils.successResponse(shipment);
    }
    
    @GetMapping(value = ApiConstants.API_PREFIX + "/shipments/{id}/label", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<byte[]> printShippingLabel(@PathVariable UUID id) {
        log.info("Generating shipping label for shipment: {}", id);
        byte[] labelData = shipmentService.generateShippingLabel(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=shipping-label-" + id + ".pdf")
                .body(labelData);
    }
    
    /**
     * Endpoint to fulfill an order asynchronously, based on the Haravan API spec
     */
    @PostMapping(ApiConstants.API_PREFIX + "/fulfillment_api/orders/{orderId}/fulfillment_async")
    public ResponseEntity<RestResponse<Map<String, Object>>> fulfillOrderAsync(
            @PathVariable UUID orderId,
            @Valid @RequestBody FulfillmentRequest fulfillmentRequest) {
        
        log.info("Creating fulfillment for order: {} with carrier package: {}", 
                orderId, fulfillmentRequest.getPackageId());
        
        Map<String, Object> result = shipmentService.createFulfillmentForOrder(orderId, fulfillmentRequest);
        
        return RestResponseUtils.createdResponse(result);
    }
} 