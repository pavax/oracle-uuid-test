package ch.mimacom.apps.config.importjob;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "batch.import" )
public class ImportBatchJobSettings {

    @NotNull
    private Integer chunkSize;

    @NotNull
    private Integer maxPersonsToCreate;

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getMaxPersonsToCreate() {
        return maxPersonsToCreate;
    }

    public void setMaxPersonsToCreate(Integer maxPersonsToCreate) {
        this.maxPersonsToCreate = maxPersonsToCreate;
    }
}