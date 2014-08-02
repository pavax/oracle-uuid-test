package ch.mimacom.apps.config.importjob;

import ch.mimacom.apps.domain.Address;
import ch.mimacom.apps.domain.Person;
import ch.mimacom.apps.internal.TimeLoggingChunkListener;
import ch.mimacom.apps.internal.XSRandom;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.persistence.EntityManagerFactory;
import java.util.Random;

@Configuration
public class ImportBatchConfiguration {

    @Bean
    public ItemReader<Person> personItemCreator(ImportBatchJobSettings importBatchJobSettings) {
        final int maxPersonsToCreate = importBatchJobSettings.getMaxPersonsToCreate();
        return new ItemReader<Person>() {
            int counter = 0;
            Random random = new XSRandom(84758247582475L);

            @Override
            public Person read() throws Exception {
                if (counter >= maxPersonsToCreate) {
                    return null;
                }
                counter++;
                Address[] addresses = new Address[createRandomInt(3, 1)];
                for (int i = 0; i < addresses.length; i++) {
                    addresses[i] = createRandomAddress();
                }
                return new Person(createRandomLengthRandomString(33, 6), createRandomLengthRandomString(25, 10), addresses);
            }

            private String createRandomLengthRandomString(int max, int min) {
                return RandomStringUtils.random(createRandomInt(max, min), 0, 0, true, false, null, random);
            }

            private Address createRandomAddress() {
                return new Address(createFixedLengthRandomString(12), createFixedLengthRandomString(15), RandomStringUtils.randomNumeric(4));
            }

            private String createFixedLengthRandomString(int count) {
                return RandomStringUtils.random(count, 0, 0, true, false, null, random);
            }

            private int createRandomInt(int max, int min) {
                return random.nextInt((max - min) + 1) + min;
            }
        };
    }

    @Bean
    public ItemWriter<Person> personJpaItemWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Person> writer = new JpaItemWriter<Person>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step importBatchJobStep1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> personItemCreator,
                                    ItemWriter<Person> personJpaItemWriter, ImportBatchJobSettings importBatchJobSettings) {
        DefaultTransactionAttribute step1TransactionAttributes = new DefaultTransactionAttribute();
        step1TransactionAttributes.setName("import-batch-job-step-1-transaction");
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(importBatchJobSettings.getChunkSize())
                .reader(personItemCreator)
                .writer(personJpaItemWriter)
                .listener(new TimeLoggingChunkListener())
                .transactionAttribute(step1TransactionAttributes)
                .build();
    }


    @Bean
    public Job sampleBatchJob(JobBuilderFactory jobs, Step importBatchJobStep1) {
        return jobs.get("import-batch-job")
                .incrementer(new RunIdIncrementer())
                .flow(importBatchJobStep1)
                .end()
                .build();
    }

}
