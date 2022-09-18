package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Essence;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class Storage<T extends Essence> {

    private final Map<Long, T> essences = new HashMap<>();
    public T save(T t){
        long id = generateUniqueID();
        t.setId(id);
        essences.put(id, t);
        return t;
    }

    public T update(Long id, T essence){
        essences.put(id, essence);
        return essences.get(id);
    }

    public void remove(Long id){
        essences.remove(id);
    }

    public Map<Long,T> getAll(){
        return essences;
    }

    public T get(Long id){
        return essences.get(id);
    }


    private long generateUniqueID(){
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
