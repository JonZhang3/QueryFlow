package com.queryflow.spring;

import com.queryflow.accessor.A;
import com.queryflow.common.QueryFlowException;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Jon
 * @since 1.2.0
 */
//@Configuration
public class QueryFlowTransactionManager extends AbstractPlatformTransactionManager {

    public QueryFlowTransactionManager() {
        this.setNestedTransactionAllowed(true);
        this.setRollbackOnCommitFailure(true);
    }

    @Override
    protected Object doGetTransaction() throws TransactionException {
        TransactionObject object = new TransactionObject();

        return object;
    }



    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
        Connection conn = null;
        try {
            String[] dataSourceTags = null;

        } catch (Throwable e) {

        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
        TransactionObject object = (TransactionObject) status.getTransaction();
        String[] dataSourceTags = object.getDataSourceTags();
        try {
            if (dataSourceTags == null || dataSourceTags.length == 0) {
                A.commit();
            } else {
                for (String dataSourceTag : dataSourceTags) {
                    A.tag(dataSourceTag).commit();
                }
            }
        } catch (Throwable e) {

            throw new QueryFlowException("could not open connection transaction", e);
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {

    }

    private void prepareConnection(Connection conn, TransactionDefinition definition) throws SQLException {
        if (definition != null) {
            if (definition.isReadOnly()) {
                conn.setReadOnly(true);
            }
            if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT
                && definition.getIsolationLevel() != conn.getTransactionIsolation()) {
                conn.setTransactionIsolation(definition.getIsolationLevel());
            }
        }
    }

    private static class TransactionObject {
        private String[] dataSourceTags;

        public String[] getDataSourceTags() {
            return dataSourceTags;
        }

        public void setDataSourceTags(String[] dataSourceTags) {
            this.dataSourceTags = dataSourceTags;
        }
    }

}
