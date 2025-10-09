package hanniejewelry.vn.payment.mapper;

import hanniejewelry.vn.payment.dto.TransactionRequest;
import hanniejewelry.vn.payment.entity.Transaction;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionDate", expression = "java(stringToInstant(request.getTransactionDate()))")
    @Mapping(target = "transferAmount", expression = "java(toBigDecimal(request.getTransferAmount()))")
    @Mapping(target = "accumulated", expression = "java(toBigDecimal(request.getAccumulated()))")
    Transaction toEntity(TransactionRequest request);

    default Instant stringToInstant(String value) {
        try {
            if (value == null) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);
            return localDateTime.toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            return null;
        }
    }

    default BigDecimal toBigDecimal(String value) {
        try {
            return value == null ? null : new BigDecimal(value);
        } catch (Exception e) {
            return null;
        }
    }
}
