package com.zoombank.wallet_api.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller stereotype for Customers
 */
@RestController
@RequestMapping("/customers")
public class CustomersController {

    @Autowired
    private CustomersService customersService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity create(@RequestBody Customer aCustomer){
        Customer createdCustomer = this.customersService.create(aCustomer);
        return new ResponseEntity(createdCustomer, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable String id, @RequestBody Customer aCustomer) {
        aCustomer.setCustomerId(id);
        Customer updatedCustomer = this.customersService.update(aCustomer);
        return new ResponseEntity(updatedCustomer, HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        this.customersService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity fetch(@PathVariable String id) {
        return new ResponseEntity(customersService.getById(id), HttpStatus.OK);
    }

}
