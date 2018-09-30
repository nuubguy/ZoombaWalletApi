package com.zoombank.wallet_api.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller stereotype for Customers
 */
@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody TransactionTransferObject aTransaction){
        Transaction createdTransaction = transactionsService.create(Transaction.createFromTransferObject(aTransaction));
        return new ResponseEntity(createdTransaction, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity get(@RequestParam(value = "accountId") String accountId, @RequestParam(value = "limitResultFromLatest", required = false) Integer limitResultFromLatest) {
        if (limitResultFromLatest != null){
            return new ResponseEntity(this.transactionsService.fetchLatestByAccount(accountId, limitResultFromLatest), HttpStatus.OK);
        }
        return new ResponseEntity(this.transactionsService.fetchAllByAccount(accountId), HttpStatus.OK);
    }


}
