package ch.mimacom.apps.internal;


import org.slf4j.Logger;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.util.concurrent.TimeUnit;

public class TimeLoggingChunkListener implements ChunkListener {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("ChunkListener");

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
}
