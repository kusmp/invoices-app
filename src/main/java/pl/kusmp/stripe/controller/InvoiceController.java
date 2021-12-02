package pl.kusmp.stripe.controller;

import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.kusmp.stripe.model.InvoiceDTO;
import pl.kusmp.stripe.service.CustomerService;
import pl.kusmp.stripe.service.InvoiceService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final CustomerService customerService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, CustomerService customerService) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
    }

    @GetMapping(path = "/v1/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable String invoiceId) {
        try {
            return new ResponseEntity<>(invoiceService.getInvoice(invoiceId), HttpStatus.OK);
        } catch (StripeException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/v1")
    public InvoiceDTO createInvoice(@RequestBody @Valid InvoiceDTO invoiceDTO) throws StripeException {
        return invoiceService.createInvoice(invoiceDTO);
    }

    @PostMapping(path = "/v1/customer")
    public String createCustomer() throws StripeException {
        return invoiceService.createCustomer();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
