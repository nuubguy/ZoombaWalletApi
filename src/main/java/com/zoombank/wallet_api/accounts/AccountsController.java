package com.zoombank.wallet_api.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping("/customers/{id}/accounts")
    public ResponseEntity update(@PathVariable String id, @RequestBody AccountPayeeRepresentation accountPayeeRepresentation) {
        Account updatedAccount = accountsService.updatePayee(id, accountPayeeRepresentation);
        return new ResponseEntity(updatedAccount, HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @GetMapping("/customers/{customerId}/accounts/{accountId}")
    public ResponseEntity get(@PathVariable String customerId, @PathVariable String accountId) {
        Account account = this.accountsService.getById(accountId);
        return new ResponseEntity(account, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/accounts")
    public ResponseEntity get(@RequestParam(value = "accountId") String accountId) {
        Account targetAccount = this.accountsService.getById(accountId);
        AccountSummaryRepresentation account = targetAccount.getRepresentation();
        return new ResponseEntity(account, HttpStatus.OK);
    }
}
