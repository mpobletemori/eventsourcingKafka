package com.banking.cqrs.core.domain;

import com.banking.cqrs.core.events.BaseEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* Clase que gestiona la lista de eventos
*
* */
public abstract class AggregateRoot {
    protected String id;
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    public String getId(){
        return this.id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BaseEvent> getUncommitedChanges(){
        return this.changes;
    }
    /*
    * Si los cambios fueron ejecutados la lista changes esta en blanco
    * */
    public void markChangesAsCommitted(){
        this.changes.clear();
    }
    /*
    * Ejecuta evento en caso es nuevo se agrega en lista
    * */
    protected void applyChange(BaseEvent event,Boolean isNewEvent){
        try {
            var method = getClass().getDeclaredMethod("apply",event.getClass());
            method.setAccessible(true);
            method.invoke(this,event);
        }catch (NoSuchMethodException ex){
            this.logger.log(Level.WARNING, MessageFormat.format("El metodo apply no fue encontrado para {0}",event.getClass().getName()));
        }catch (Exception e){
            this.logger.log(Level.SEVERE,"Errores aplicando el evento al aggregate",e);
        }finally {
            if(isNewEvent){
                this.changes.add(event);
            }
        }
    }

    /*
    * Ejecutar evento y registrarlo
    * */
    public void raiseEvent(BaseEvent event){
         this.applyChange(event,true);
    }

    /*
    * Ejecutar lista de eventos existentes
    *
    * */
    public void replayEvents(Iterable<BaseEvent> events){
        events.forEach(event->applyChange(event,false));

    }
}
