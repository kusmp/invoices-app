package pl.kusmp.stripe.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceItemDTO {
    private String customer;
    private Long amount;
    private String currency;
}
