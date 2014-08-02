package ch.mimacom.apps.config.export;

import ch.mimacom.apps.domain.Person;
import ch.mimacom.apps.domain.QPerson;
import ch.mimacom.apps.internal.TimeLoggingChunkListener;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Configuration
public class ExportBatchJobConfiguration {

    @Bean
    public ItemReader<Person> jpaItemReader(EntityManagerFactory entityManagerFactory,ExportBatchJobSettings exportBatchJobSettings) {
        JpaPagingItemReader<Person> itemReader = new JpaPagingItemReader<Person>();
        itemReader.setEntityManagerFactory(entityManagerFactory);
        //itemReader.setQueryString("select p from Person as p");
        itemReader.setPageSize(exportBatchJobSettings.getPageReadSize());
        itemReader.setQueryProvider(new AbstractJpaQueryProvider() {
            @Override
            public void afterPropertiesSet() throws Exception {}

            @Override
            public Query createQuery() {
                QPerson person = QPerson.person;
                JPAQuery query = new JPAQuery(getEntityManager());
                return query.from(person).createQuery(person);
            }
        });
        return itemReader;
    }

    @Bean
    public ItemWriter<Person> personFlatFileItemWriter(ExportBatchJobSettings exportBatchJobSettings) {
        FlatFileItemWriter<Person> itemWriter = new FlatFileItemWriter<Person>();
        itemWriter.setResource(exportBatchJobSettings.getFile());
        FormatterLineAggregator<Person> lineAggregator = new FormatterLineAggregator<Person>();
        BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<Person>();
        fieldExtractor.setNames(new String[]{"personId.value","name"});
        lineAggregator.setFieldExtractor(fieldExtractor);
        lineAggregator.setFormat("%-36s|%-25S|");
        itemWriter.setLineAggregator(lineAggregator);

        return itemWriter;
    }

    @Bean
    public Step exportBatchJob1(StepBuilderFactory stepBuilderFactory,
                                ItemReader<Person> jpaItemReader,
                                ItemWriter<Person> personFlatFileItemWriter,
                                ExportBatchJobSettings exportBatchJobSettings) {
        DefaultTransactionAttribute step1TransactionAttributes = new DefaultTransactionAttribute();
        step1TransactionAttributes.setName("export-batch-job-1-transaction");
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(exportBatchJobSettings.getChunkSize())
                .reader(jpaItemReader)
                .writer(personFlatFileItemWriter)
                .listener(new TimeLoggingChunkListener())
                .transactionAttribute(step1TransactionAttributes)
                .build();
    }

    @Bean
    public Job exportBatchJob(JobBuilderFactory jobs, Step exportBatchJob1) {
        return jobs.get("export-batch-job")
                .incrementer(new RunIdIncrementer())
                .flow(exportBatchJob1)
                .end()
                .build();
    }
}
