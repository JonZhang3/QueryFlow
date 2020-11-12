package com.queryflow.spring;

import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;

import java.io.Serializable;

public class QFTransactionAttribute extends DefaultTransactionAttribute implements Serializable {

    private final RuleBasedTransactionAttribute baseAttribute;
    private String[] dataSourceTags;
    private String[] excludedDataSourceTags;

    public QFTransactionAttribute(RuleBasedTransactionAttribute baseAttribute) {
        this.baseAttribute = baseAttribute;
    }

    public RuleBasedTransactionAttribute getBaseAttribute() {
        return baseAttribute;
    }

    public String[] getDataSourceTags() {
        return dataSourceTags;
    }

    public void setDataSourceTags(String[] dataSourceTags) {
        this.dataSourceTags = dataSourceTags;
    }

    public String[] getExcludedDataSourceTags() {
        return excludedDataSourceTags;
    }

    public void setExcludedDataSourceTags(String[] excludedDataSourceTags) {
        this.excludedDataSourceTags = excludedDataSourceTags;
    }
}
