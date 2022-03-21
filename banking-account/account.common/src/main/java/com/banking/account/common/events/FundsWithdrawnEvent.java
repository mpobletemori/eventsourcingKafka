package com.banking.account.common.events;

import com.banking.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
/*
* Evento dinero retirado de cuenta
*
* */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FundsWithdrawnEvent extends BaseEvent {
    //monto a retirar
    private double amount;
}
