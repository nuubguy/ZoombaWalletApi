package com.example.assignment04.customers;

import com.example.assignment04.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service stereotype for Customers
 */
@Service
public class CustomersService extends BaseService<Customer> {

    private static final String idPrefix = "C";
    private final String cashAccountCustomerId = "CASH_ACCOUNT";

    @Override
    protected String preFix() {
        return idPrefix;
    }

    @Autowired
    CustomersRepository customersRepository;

    @Override
    public Customer create(Customer target) {
        target.setCustomerId(this.createID());
        customersRepository.save(target);
        return target;
    }

    public Customer getBankCashAccountCustomer(){
        Customer result;
        try {
            result = this.getById(this.cashAccountCustomerId);
        }
        catch (CustomerNotFoundException ex){
            result = new Customer("Cash Account", "Cash Account for Bank");
            result.setCustomerId(this.cashAccountCustomerId);
            customersRepository.save(result);
        }
        return result;
    }

    public Customer update(Customer target) {
        Customer targetCustomer = this.getById(target.getCustomerId());
        targetCustomer.setInfo(target.getInfo());
        targetCustomer.setName(target.getName());
        return this.customersRepository.save(target);
    }

    public void delete(String id) {
        Customer targetCustomer = this.getById(id);
        targetCustomer.setDisabled(true);
        this.customersRepository.save(targetCustomer);

    }

    public Customer getById(String id) {
        Optional<Customer> result = this.customersRepository.findById(id);
        if(!result.isPresent()) throw new CustomerNotFoundException();
        return result.get();
    }
}
