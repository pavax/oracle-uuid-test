package ch.mimacom.apps.domain;

import com.fasterxml.uuid.EthernetAddress;
import org.junit.Test;

import java.util.UUID;

public class IdGeneratorTest {

    @Test
    public void testGenerateId() throws Exception {
        final EthernetAddress ETHERNET_ADDRESS = EthernetAddress.fromInterface();
        for (int i = 0; i < 5; i++) {
            System.out.println(IdGenerator.timeBasedUUID());
        }
    }

    @Test
    public void testManualUUID() throws Exception {
        System.out.println(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testManualUUIDInvalid() throws Exception {
        System.out.println(UUID.fromString("0000-0000-0000-0000"));
    }
}