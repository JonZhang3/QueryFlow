package com.queryflow.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 *
 * @since 1.2.0
 * @author Jon
 */
@Configuration
public class QueryFlowTransactionManager extends AbstractPlatformTransactionManager {

    public QueryFlowTransactionManager() {
        this.setNestedTransactionAllowed(true);
        this.setRollbackOnCommitFailure(true);
    }

    @Override
    protected Object doGetTransaction() throws TransactionException {
        return null;
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {

    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {

    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {

    }
}
