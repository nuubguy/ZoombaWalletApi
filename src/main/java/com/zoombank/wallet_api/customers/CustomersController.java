package com.zoombank.wallet_api.customers;

import com.zoombank.wallet_api.BaseController;
import com.zoombank.wallet_api.accounts.Account;
import com.zoombank.wallet_api.accounts.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller stereotype for Customers
 */
@RestController
@RequestMapping("/customers")
public class CustomersController extends BaseController {

    @Autowired
    private CustomersService customersService;

    @Autowired
    private AccountsRepository accountsRepository;

    @CrossOrigin
    @PostMapping
    public ResponseEntity create(@RequestBody Customer aCustomer){
        Customer createdCustomer = this.customersService.create(aCustomer);
        return new ResponseEntity(createdCustomer, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable String id, @RequestBody Customer aCustomer) {
        if (checkAuthentication(id)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        aCustomer.setCustomerId(id);
        Customer updatedCustomer = this.customersService.update(aCustomer);
        return new ResponseEntity(updatedCustomer, HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        if (checkAuthentication(id)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        this.customersService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity fetch(@PathVariable String id) {
        if (checkAuthentication(id)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        Customer customer = customersService.getById(id);
        CustomerRepresentation result = new CustomerRepresentation(customer.getCustomerId(), customer.getName());
        List<Account> accountList = accountsRepository.getAllByCustomer_CustomerId(id);
        result.setAccountList(accountList);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
