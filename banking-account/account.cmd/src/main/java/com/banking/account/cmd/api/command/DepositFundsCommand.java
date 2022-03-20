package com.banking.account.cmd.api.command;

import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;
/*
* Depositar dinero
* */
@Data
public class DepositFundsCommand extends BaseCommand {
    private double amount;
}
