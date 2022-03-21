package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FundsDepositedEvent;
import com.banking.account.common.events.FundsWithdrawnEvent;
import com.banking.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
* Implementacion de aggregate concreto que gestiona los estados
* */
@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    //abrir cuenta bancaria
    public AccountAggregate(OpenAccountCommand command){
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createdDate(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event){
        this.id= event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    //Ejecuta el deposito de dinero
    public void depositFunds(double amount){
        if(!this.active){
            throw new IllegalStateException("Los fondos no pueden ser depositados en esta cuenta");
        }

        if(amount < 0){
            throw new IllegalStateException("El deposito de dinero no puede ser cero o menor a cero");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    /*
    * aplicar deposito de dinero
    * */
    public void apply(FundsDepositedEvent event){
        this.id = event.getId();
        this.balance+= event.getAmount();
    }

    /*
    * Ejecuta evento de retiro de dinero
    *
    *
    * */
    public void withDrawFunds(double amount){
        if(!this.active){
            throw new IllegalStateException("La cuenta bancaria esta cerrada");
        }

        if(amount < 0){
            throw new IllegalStateException("El retiro de dinero no puede ser cero o menor a cero");
        }

        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }
    /*
    * Aplica retiro de dinero en cuenta de ahorros
    * */
    public void apply(FundsWithdrawnEvent event){
        this.id = event.getId();
        this.balance-= event.getAmount();
    }

    /*
    * Ejecuta cierre de cuenta corriente
    * */
    public void closeAccount(){
        if(!this.active){
            throw new IllegalStateException("La cuenta bancaria esta cerrada");
        }

        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    /*
    * Aplica cierre de cuenta corriente
    * */
    public void apply(AccountClosedEvent event){
        this.id= event.getId();
        this.active = false;
    }
}
