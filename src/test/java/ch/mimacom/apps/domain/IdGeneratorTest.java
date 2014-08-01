package ch.mimacom.apps.domain;

import java.util.UUID;

public class IdGeneratorTest {

    @org.junit.Test
    public void testGenerateId() throws Exception {
        UUID uuid = IdGenerator.timeBasedUUID();
        System.out.println(uuid);
    }
}