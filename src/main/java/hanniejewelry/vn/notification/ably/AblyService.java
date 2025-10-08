package hanniejewelry.vn.notification.ably;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.types.AblyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AblyService {
    private final AblyRealtime ably;

    @Autowired
    public AblyService(@Value("${ably.api-key}") String ablyApiKey) throws AblyException {
        log.info("Initializing Ably service");
        this.ably = new AblyRealtime(ablyApiKey);
    }

    public void publishOrderPaid(String orderCode) throws AblyException {
        log.info("Publishing order paid event to Ably for order: {}", orderCode);
        String message = "Order " + orderCode + " paid successfully!";
        String channelName = "order-" + orderCode;

        log.debug("Publishing to channel: {}, event: {}, message: {}", channelName, "order-paid", message);
        ably.channels.get(channelName).publish("order-paid", message);
        log.info("Successfully published order paid event for order: {}", orderCode);
    }
}
