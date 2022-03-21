package com.banking.account.cmd.api.command;

import com.banking.cqrs.core.commands.BaseCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/*
* Depositar dinero
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DepositFundsCommand extends BaseCommand {
    //monto a depositar
    private double amount;
}
