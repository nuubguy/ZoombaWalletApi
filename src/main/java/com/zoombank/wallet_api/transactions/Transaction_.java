package com.zoombank.wallet_api.transactions;
import com.zoombank.wallet_api.accounts.Account;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;
import java.util.Currency;
@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public class Transaction_ {

    public static volatile SingularAttribute<Transaction, Account> debit;
    public static volatile SingularAttribute<Transaction, Account> credit;
    public static volatile SingularAttribute<Transaction, Double> amount;
    public static volatile SingularAttribute<Transaction, Currency> currency;
    public static volatile SingularAttribute<Transaction, String> transactionId;
    public static volatile SingularAttribute<Transaction, LocalDateTime> dateTime;
    public static volatile SingularAttribute<Transaction, String> description;

}
