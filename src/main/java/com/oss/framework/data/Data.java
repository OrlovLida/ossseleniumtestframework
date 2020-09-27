package com.oss.framework.data;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class Data {

    private final boolean isList;
    private final List<DataWrapper> values;

    public static Data createFindFirst(String readableValue) {
        DataWrapper data = new DataWrapper(readableValue, readableValue);
        return new Data(data);
    }

    public static Data createMultiData(List<String> values) {
        List<DataWrapper> data = values.stream()
                .map(v-> new DataWrapper(v, v)).collect(Collectors.toList());
        return new Data(data);
    }

    public static Data createSingleData(String value) {
        DataWrapper data = new DataWrapper(value, value);
        return new Data(data);
    }

    public static Data createSingleData(String value, String readableValue) {
        DataWrapper data = new DataWrapper(value, readableValue);
        return new Data(data);
    }

    public static Data createMultiDataWrapper(List<DataWrapper> dataWrappers) {
        return new Data(dataWrappers);
    }

    private Data(List<DataWrapper> values) {
        this.isList = true;
        this.values = Lists.newArrayList(values);
    }

    private Data(DataWrapper values) {
        this.isList = false;
        this.values = Lists.newArrayList(values);
    }

    public boolean isList() {
        return this.isList;
    }

    public String getStringValue() {
        return this.values.get(0).getValue();
    }

    public List<String> getStringValues() {
        return this.values.stream().map(DataWrapper::getValue).collect(Collectors.toList());
    }

    public DataWrapper getWrapper() {
        return values.get(0);
    }

    public static class DataWrapper {
        private final String value;
        private final String readableValue;
        private final boolean findFirst;

        public static DataWrapper create(String value, String readableValue) {
            return new DataWrapper(value, readableValue, false);
        }

        public static DataWrapper createFindFirst(String value, String readableValue) {
            return new DataWrapper(value, readableValue, true);
        }

        private DataWrapper(String value, String readableValue) {
            this.value = value;
            this.readableValue = readableValue;
            this.findFirst = false;
        }

        private DataWrapper(String value, String readableValue, boolean findFirst) {
            this.value = value;
            this.readableValue = readableValue;
            this.findFirst = findFirst;
        }

        public boolean isFindFirst() {
            return findFirst;
        }

        public String getValue() {
            return value;
        }

        public String getReadableValue() {
            return readableValue;
        }


    }

}
