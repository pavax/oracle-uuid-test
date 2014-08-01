package ch.mimacom.apps.config;

import ch.mimacom.apps.domain.Person;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.persistence.EntityManagerFactory;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableBatchProcessing
public class SampleBatchConfiguration {

    @Bean
    public ItemReader<Person> reader2(SampleBatchJobSettings sampleBatchJobSettings) {
        final int maxPersonsToCreate = sampleBatchJobSettings.getMaxPersonsToCreate();
        return new ItemReader<Person>() {
            int counter = 0;

            @Override
            public Person read() throws Exception {

                if (counter >= maxPersonsToCreate) {
                    return null;
                }
                counter++;
                return new Person(RandomStringUtils.randomAlphabetic(25));
            }
        };
    }

    @Bean
    public ItemWriter<Person> writer(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Person> writer = new JpaItemWriter<Person>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader,
                      ItemWriter<Person> writer, SampleBatchJobSettings sampleBatchJobSettings) {
        DefaultTransactionAttribute step1TransactionAttributes = new DefaultTransactionAttribute();
        step1TransactionAttributes.setName("sample-batch-step1-transaction");
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(sampleBatchJobSettings.getChunkSize())
                .reader(reader)
                .writer(writer)
                .listener(buildChunkListener())
                .transactionAttribute(step1TransactionAttributes)
                .build();
    }

    private ChunkListener buildChunkListener() {
        return new ChunkListener() {

            private final Logger logger = org.slf4j.LoggerFactory.getLogger("ChunkListener");

            private static final String CHUNK_BEFORE_TIME = "chunkBeforeTime";

            private int counter = 0;

            @Override
            public void beforeChunk(ChunkContext context) {
                context.setAttribute(CHUNK_BEFORE_TIME, System.nanoTime());
            }

            @Override
            public void afterChunk(ChunkContext context) {
                long chunkStartTime = (Long) context.getAttribute(CHUNK_BEFORE_TIME);
                long convert = TimeUnit.MILLISECONDS.convert(System.nanoTime() - chunkStartTime, TimeUnit.NANOSECONDS);
                logger.info("Chunk Nr: " + counter + " took: " + convert + "ms");
                counter++;
            }

            @Override
            public void afterChunkError(ChunkContext context) {

            }
        };
    }

    @Bean
    public Job sampleBatchJob(JobBuilderFactory jobs, Step step1) {
        return jobs.get("sample-batch-job")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

}
