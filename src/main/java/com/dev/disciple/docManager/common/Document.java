package com.dev.disciple.docManager.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;


public class Document {
    private Map<String, Object> documentMap = new HashMap<>();

    public Document() {

    }

    public Document(@NotNull Map<String, Object> inputMap) {
        this.documentMap.putAll(inputMap);
    }

    public Map<String, Object> getMap() {
        return this.documentMap;
    }

    public void setMap(Map<String, Object> map) {
        this.documentMap = map;
    }

    public void put(String key, Object value) {
        documentMap.put(key, value);
    }

    public Object get(String key) {
        return documentMap.get(key);
    }

    public void remove(String key) {
        documentMap.remove(key);
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
