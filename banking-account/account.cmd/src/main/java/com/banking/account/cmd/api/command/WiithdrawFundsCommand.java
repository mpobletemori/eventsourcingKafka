package com.banking.account.cmd.api.command;

import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;
/**
 * Retirar dinero de cuenta
 */

@Data
public class WiithdrawFundsCommand extends BaseCommand {
    //monto a retirar
    private double amount;
}
