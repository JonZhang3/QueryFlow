package com.queryflow.spring;

import com.queryflow.annotation.DataSource;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;

public class QFTransactionAnnotationParser extends SpringTransactionAnnotationParser {

    private static final String KEY_DATASOURCE_TAGS = "dataSourceTags";
    private static final String KEY_EXELUDED_TAGS = "excludedTags";

    @Override
    protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
        RuleBasedTransactionAttribute attribute = (RuleBasedTransactionAttribute) super.parseTransactionAnnotation(attributes);
        QFTransactionAttribute result = new QFTransactionAttribute(attribute);
        result.setDataSourceTags(attributes.getStringArray(KEY_DATASOURCE_TAGS));
        result.setExcludedDataSourceTags(attributes.getStringArray(KEY_EXELUDED_TAGS));
        return result;
    }

    @Override
    public TransactionAttribute parseTransactionAnnotation(Transactional ann) {

        return super.parseTransactionAnnotation(ann);
    }

    @Override
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement element) {
        AnnotationAttributes attributes =
            AnnotatedElementUtils.findMergedAnnotationAttributes(element, Transactional.class, false, false);
        if (attributes == null) {
            return null;
        }
        DataSource dataSource = element.getAnnotation(DataSource.class);
        String[] dataSourceTags;
        String[] excludedTags;
        if (dataSource == null) {
            dataSourceTags = new String[0];
            excludedTags = new String[0];
        } else {
            dataSourceTags = dataSource.value();
            excludedTags = dataSource.excluded();
        }
        attributes.put(KEY_DATASOURCE_TAGS, dataSourceTags);
        attributes.put(KEY_EXELUDED_TAGS, excludedTags);
        return this.parseTransactionAnnotation(attributes);
    }
}
