package com.banking.cqrs.core.producers;

import com.banking.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void producer(String topic, BaseEvent event);
}
