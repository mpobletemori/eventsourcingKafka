package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.cmd.api.dto.OpenAccountResponse;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path="/api/v1/openBankAccount")
public class OpenAccountController {
    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command){
        var id = UUID.randomUUID().toString();
        command.setId(id);
        try {
            this.commandDispatcher.sendCommand(command);
            return new ResponseEntity<>(new OpenAccountResponse("La cuenta del banco se ha creado exitosamente",id), HttpStatus.CREATED);
        }catch (IllegalStateException e){
            logger.log(Level.WARNING, MessageFormat.format("No se pudo generar la cuenta de banco - {0}",e.toString()));
            return new ResponseEntity<>(new OpenAccountResponse(e.toString(),id), HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            var safeErrorMessage = MessageFormat.format("Errores mientras procesaba el request - {0}",id);
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new OpenAccountResponse(safeErrorMessage,id), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
