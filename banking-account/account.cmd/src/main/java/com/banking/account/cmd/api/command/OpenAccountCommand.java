package com.banking.account.cmd.api.command;

import com.banking.account.common.dto.AccountType;
import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;
/*
* Clase para abrir cuenta bancaria
*
* */
@Data
public class OpenAccountCommand extends BaseCommand {
    //nombre de cliente
    private String accountHolder;
    //tipo de cuenta
    private AccountType accountType;
    //monto a aperturar
    private double openingBalance;
}
