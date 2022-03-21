package com.banking.account.common.events;

import com.banking.account.common.dto.AccountType;
import com.banking.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/*
* Evento de cuenta creada (se escribe en pasado)
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountOpenedEvent extends BaseEvent {
    //nombre de cliente
    private String accountHolder;
    //tipo de cuenta
    private AccountType accountType;
    //fecha de creacion
    private Date createdDate;
    //monto a aperturar
    private double openingBalance;
}
