package com.banking.account.cmd.infraestructure;

import com.banking.cqrs.core.commands.BaseCommand;
import com.banking.cqrs.core.commands.CommandHandlerMethod;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type,c->new LinkedList<>());
        handlers.add(handler);

    }

    @Override
    public void sendCommand(BaseCommand command) {
        var handlers = this.routes.get(command.getClass());
        if(CollectionUtils.isEmpty(handlers)){
            throw new RuntimeException(" El comando handler no fue registrado ");
        }

        if(handlers.size()>0){
            throw new RuntimeException(" No puede enviar un command con mas de un handler ");
        }

        handlers.get(0).handle(command);
    }
}
