package es.poc.bid.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mcalavera81 on 12/01/16.
 */
public class Session {

    public Session(String id){
        this.id = id;
    }

    private String id;

    private Map<String, Object> sessionAttributes = new ConcurrentHashMap<>();

    public String getId() {
        return id;
    }

    public Object getAttribute(String name){
        return sessionAttributes.get(name);
    }

    public void setAttribute(String name, Object value){
        sessionAttributes.put(name, value);
    }
}
