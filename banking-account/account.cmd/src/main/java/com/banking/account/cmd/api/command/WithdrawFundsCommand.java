package com.banking.account.cmd.api.command;

import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;
/**
 * Retirar dinero de cuenta
 */

@Data
public class WithdrawFundsCommand extends BaseCommand {
    //monto a retirar
    private double amount;
}
