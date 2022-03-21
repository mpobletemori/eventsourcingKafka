package com.banking.account.common.events;

import com.banking.cqrs.core.events.BaseEvent;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/*
* Evento cuenta cerrada
* */
@Data
@SuperBuilder
public class AccountClosedEvent extends BaseEvent {

}