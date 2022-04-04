package com.banking.account.query.infraestructure.handlers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.account.query.domain.AccountRepository;
import com.banking.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AccountEventHandler implements EventHandler{

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Override
    public void on(AccountOpenedEvent event) {
        this.accountRepository.save(BankAccount.builder()
                        .id(event.getId())
                        .accountHolder(event.getAccountHolder())
                        .creationDate(event.getCreatedDate())
                        .accountType(event.getAccountType())
                        .balance(event.getOpeningBalance())
                .build());
    }

    @Transactional
    @Override
    public void on(FundsDepositedEvent event) {
        //inicio deposito de dinero

        //obtener cuenta bancaria
        var bankAccount = this.accountRepository.findById(event.getId());
        if(bankAccount.isEmpty()){
            return;
        }

        //obtener saldo actual en cuenta bancaria
        var currentBalance = bankAccount.get().getBalance();

        //Calcular el nuevo saldo en cuenta bancaria
        var latestBalance = currentBalance + event.getAmount();
        bankAccount.get().setBalance(latestBalance);

        //Actualizar en BD el saldo
        this.accountRepository.save(bankAccount.get());
    }

    @Transactional
    @Override
    public void on(FundsWithdrawnEvent event) {
        //inicio retiro de dinero

        //obtener cuenta bancaria
        var bankAccount = this.accountRepository.findById(event.getId());
        if(bankAccount.isEmpty()){
            return;
        }

        //obtener saldo actual en cuenta bancaria
        var currentBalance = bankAccount.get().getBalance();

        //Calcular el nuevo saldo en cuenta bancaria
        var latestBalance = currentBalance - event.getAmount();
        bankAccount.get().setBalance(latestBalance);

        //Actualizar en BD el saldo
        this.accountRepository.save(bankAccount.get());
    }

    @Transactional
    @Override
    public void on(AccountClosedEvent event) {
        this.accountRepository.deleteById(event.getId());
    }
}
