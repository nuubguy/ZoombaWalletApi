package com.zoombank.wallet_api.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller stereotype for Accounts
 */
@RestController
public class AccountsController {

    @Autowired
    AccountsService accountsService;

    @CrossOrigin
    @PostMapping("/customers/{id}/accounts")
    public ResponseEntity create(@PathVariable String id, @RequestBody Account account) {
        Account createdAccount = accountsService.create(id, account);
        return new ResponseEntity(createdAccount, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/customers/{customerId}/accounts/{accountId}")
    public ResponseEntity create(@PathVariable String customerId, @PathVariable String accountId) {
        Account account = this.accountsService.getById(accountId);
        return new ResponseEntity(account, HttpStatus.OK);
    }
}
