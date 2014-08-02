package ch.mimacom.apps.config.export;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "batch.export" )
public class ExportBatchJobSettings {
    @NotNull
    private Integer chunkSize;

    @NotNull
    private Integer pageReadSize;

    private Resource file;

    public Resource getFile() {
        return file;
    }

    public void setFile(Resource file) {
        this.file = file;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getPageReadSize() {
        return pageReadSize;
    }

    public void setPageReadSize(Integer pageReadSize) {
        this.pageReadSize = pageReadSize;
    }
}
