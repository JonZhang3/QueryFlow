package com.queryflow.spring;

import com.queryflow.annotation.DataSource;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

public class QFTransactionInterceptor extends TransactionInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null;
        Method method = invocation.getMethod();
        this.invokeWithinTransaction(method, targetClass, invocation::proceed);
        return super.invoke(invocation);
    }

    @Override
    protected Object invokeWithinTransaction(Method method, Class<?> targetClass, InvocationCallback invocation) throws Throwable {
        TransactionAttributeSource tas = this.getTransactionAttributeSource();
        TransactionAttribute txAttr = tas != null ? tas.getTransactionAttribute(method, targetClass) : null;
        TransactionManager tm = this.determineTransactionManager(txAttr);
        if (tm instanceof QueryFlowTransactionManager) {

            PlatformTransactionManager ptm = this.toPlatformTransactionManager(tm);
            String joinpointIdentification = ClassUtils.getQualifiedMethodName(method, targetClass);
            DataSource annotation = method.getAnnotation(DataSource.class);
            String[] dataSourceTags;
            if(annotation != null) {
                dataSourceTags = annotation.value();
            } else {
                dataSourceTags = new String[0];
            }
            TransactionAspectSupport.TransactionInfo txInfo = this.createTransactionIfNecessary(ptm, txAttr,
                joinpointIdentification, dataSourceTags);
            Object result;
            try {
                result = invocation.proceedWithInvocation();
            } catch (Throwable t) {
                this.completeTransactionAfterThrowing(txInfo, t);
                throw t;
            } finally {
                this.cleanupTransactionInfo(txInfo);
            }
            this.commitTransactionAfterReturning(txInfo);
            return result;
        } else {
            return super.invokeWithinTransaction(method, targetClass, invocation);
        }
    }

    private PlatformTransactionManager toPlatformTransactionManager(TransactionManager tm) {
        if (tm != null && !(tm instanceof PlatformTransactionManager)) {
            throw new IllegalStateException("the transaction manager is not a PlatformTransactionManager: " + tm);
        }
        return (PlatformTransactionManager) tm;
    }


    protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm,
                                                           TransactionAttribute txAttr,
                                                           String joinpointIdentification, String[] dataSourceTags) {
        QFTransactionAttribute attribute = new QFTransactionAttribute(txAttr);
        attribute.setDataSourceTags(dataSourceTags);
        return super.createTransactionIfNecessary(tm, attribute, joinpointIdentification);
    }
}
