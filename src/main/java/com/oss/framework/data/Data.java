package com.oss.framework.data;

import java.util.List;

import com.google.common.collect.Lists;

public class Data {

    private final boolean isList;
    private final List<String> values;

    public static Data createMultiData(List<String> values) {
        return new Data(values);
    }

    public static Data createSingleData(String value) {
        return new Data(value);
    }

    private Data(List<String> values) {
        this.isList = true;
        this.values = Lists.newArrayList(values);
    }

    private Data(String values) {
        this.isList = false;
        this.values = Lists.newArrayList(values);
    }

    public boolean isList() {
        return this.isList;
    }

    public String getStringValue() {
        return this.values.get(0);
    }

    public List<String> getStringValues() {
        return this.values;
    }


}
