package com.zoombank.wallet_api.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity create(@Valid @RequestBody TransactionRepresentation aTransaction){
        Transaction createdTransaction = transactionsService.create(aTransaction);
        return new ResponseEntity(createdTransaction, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity get(@RequestParam(value = "accountId") String accountId,
                              @RequestParam(value = "limitResultFromLatest", required = false) Integer limitResultFromLatest,
                              @RequestParam(value = "description",required = false) String description,
                              @RequestParam(value = "amount",required = false) Double amount,
                              @RequestParam(value = "status",required = false) Integer status) {
        if (limitResultFromLatest != null){
            return new ResponseEntity(this.transactionsService.fetchLatestByAccount(accountId, limitResultFromLatest), HttpStatus.OK);
        }
//        if (limitResultFromLatest==null && description != null && amount==null){
//            return new ResponseEntity(this.transactionsService.fetchByDecsription(accountId,description), HttpStatus.OK);
//        }
//        if (limitResultFromLatest==null && description == null && amount!=null){
//            return new ResponseEntity(this.transactionsService.fetchByAmount(accountId,amount), HttpStatus.OK);
//        }
//        if (limitResultFromLatest==null && description != null && amount!=null){
//            return new ResponseEntity(this.transactionsService.fetchByAmountAndDescription(accountId,description,amount), HttpStatus.OK);
//        }


        List<Transaction> result = this.transactionsService.fetchAll(accountId, description,amount ,status);

        return new ResponseEntity(result, HttpStatus.OK);
//        return new ResponseEntity(this.transactionsService.fetchAllByAccount(accountId), HttpStatus.OK);
    }

}
