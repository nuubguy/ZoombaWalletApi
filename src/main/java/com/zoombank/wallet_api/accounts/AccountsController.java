package com.zoombank.wallet_api.accounts;

import com.zoombank.wallet_api.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller stereotype for Accounts
 */
@RestController
public class AccountsController extends BaseController {

    @Autowired
    AccountsService accountsService;

    @CrossOrigin
    @PostMapping("/customers/{customerId}/accounts")
    public ResponseEntity create(@PathVariable String customerId, @RequestBody Account account) {
        if (checkAuthentication(customerId)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        Account createdAccount = accountsService.create(customerId, account);
        return new ResponseEntity(createdAccount, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PutMapping("/customers/{customerId}/accounts")
    public ResponseEntity update(@PathVariable String customerId, @RequestBody AccountPayeeRepresentation accountPayeeRepresentation) {
        if (checkAuthentication(customerId)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        Account updatedAccount = accountsService.updatePayee(customerId, accountPayeeRepresentation);
        return new ResponseEntity(updatedAccount, HttpStatus.ACCEPTED);
    }

    @CrossOrigin
    @GetMapping("/customers/{customerId}/accounts/{accountId}")
    public ResponseEntity get(@PathVariable String customerId, @PathVariable String accountId) {
        if (checkAuthentication(customerId)) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
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
