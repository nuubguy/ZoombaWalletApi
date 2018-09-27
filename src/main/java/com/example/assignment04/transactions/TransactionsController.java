package com.example.assignment04.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller stereotype for Customers
 */
@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;

    @PostMapping
    public ResponseEntity create(@RequestBody Transaction aTransaction){
        Transaction createdTransaction = transactionsService.create(aTransaction);
        return new ResponseEntity(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity get(@RequestParam(value = "accountId") String accountId, @RequestParam(value = "limitResultFromLatest", required = false) Integer limitResultFromLatest) {
        if (limitResultFromLatest != null){
            return new ResponseEntity(this.transactionsService.fetchLatestByAccount(accountId, limitResultFromLatest), HttpStatus.OK);
        }
        return new ResponseEntity(this.transactionsService.fetchAllByAccount(accountId), HttpStatus.OK);
    }


}
