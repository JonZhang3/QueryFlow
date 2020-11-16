package com.queryflow.spring;

import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.io.Serializable;

public class QFTransactionAttribute extends DefaultTransactionAttribute implements Serializable {

    private final TransactionAttribute baseAttribute;
    private String[] dataSourceTags;

    public QFTransactionAttribute(TransactionAttribute baseAttribute) {
        this.baseAttribute = baseAttribute;
    }

    public TransactionAttribute getBaseAttribute() {
        return baseAttribute;
    }

    public String[] getDataSourceTags() {
        return dataSourceTags;
    }

    public void setDataSourceTags(String[] dataSourceTags) {
        this.dataSourceTags = dataSourceTags;
    }

}
