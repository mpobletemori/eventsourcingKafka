package com.banking.account.query.api.controllers;

import com.banking.account.query.api.dto.AccountLookupResponse;
import com.banking.account.query.api.dto.EqualityType;
import com.banking.account.query.api.queries.FindAccountByHolderQuery;
import com.banking.account.query.api.queries.FindAccountByIdQuery;
import com.banking.account.query.api.queries.FindAccountWithBalanceQuery;
import com.banking.account.query.api.queries.FindAllAccountsQuery;
import com.banking.account.query.domain.BankAccount;
import com.banking.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/bankAccountLookup")
public class AccountLookupController {
    private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

    @Autowired
    private QueryDispatcher queryDispatcher;

    @GetMapping("/")
    public ResponseEntity<AccountLookupResponse> getAllAccounts(){
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());
            if(CollectionUtils.isEmpty(accounts)){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Se realizo la consulta con exito")
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            var safeErrorMessage = "Errores ejecutando la consulta";
            logger.log(Level.SEVERE, safeErrorMessage,e);
                return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable("id") String id){
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByIdQuery(id));
            if(CollectionUtils.isEmpty(accounts)){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Se realizo la consulta con exito")
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            var safeErrorMessage = "Errores ejecutando la consulta";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/byHolder/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable("accountHolder") String accountHolder){
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByHolderQuery(accountHolder));
            if(CollectionUtils.isEmpty(accounts)){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Se realizo la consulta con exito")
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            var safeErrorMessage = "Errores ejecutando la consulta";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountWithBalance(@PathVariable("equalityType") EqualityType equalityType,
                                                                       @PathVariable("balance") double balance){
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountWithBalanceQuery(balance,equalityType));
            if(CollectionUtils.isEmpty(accounts)){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Se realizo la consulta con exito")
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            var safeErrorMessage = "Errores ejecutando la consulta";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
