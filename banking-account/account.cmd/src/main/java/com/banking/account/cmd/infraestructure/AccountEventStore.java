package com.banking.account.cmd.infraestructure;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.account.cmd.domain.EventStoreRepository;
import com.banking.cqrs.core.events.BaseEvent;
import com.banking.cqrs.core.events.EventModel;
import com.banking.cqrs.core.exceptions.AggregateNotFoundException;
import com.banking.cqrs.core.exceptions.ConcurrencyExcepction;
import com.banking.cqrs.core.infrastructure.EventStore;
import com.banking.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = this.eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(expectedVersion!=-1 && eventStream.get(eventStream.size()-1).getVersion()!=expectedVersion){
           throw new ConcurrencyExcepction();
        }

        var version = expectedVersion;

        for(var event:events){
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .evenData(event)
                    .build();

            var persistedEvent=this.eventStoreRepository.save(eventModel);
            if(!StringUtils.hasLength(persistedEvent.getId())){
                this.eventProducer.producer(event.getClass().getSimpleName(),event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvent(String aggregateId) {

        var eventStream = this.eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(CollectionUtils.isEmpty(eventStream)){
            throw new AggregateNotFoundException("La cuenta del banco es incorrecta");
        }

        return eventStream.stream().map(x->x.getEvenData()).collect(Collectors.toList());
    }
}
