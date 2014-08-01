package ch.mimacom.apps.domain;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Person {

//    @Id
//    @Column(length = 36)
//    private String id;

//    @Id
//    @GeneratedValue(generator = "PerSeq")
//    @SequenceGenerator(name = "PerSeq", sequenceName = "PERS_SEQ")
//    private Long id;

    @Id
    @Type(type = "org.hibernate.type.UUIDBinaryType")
    @Column(length = 16)
    private UUID id;

    private String name;

    public Person(String name) {
        this.id = IdGenerator.timeBasedUUID();
        this.name = name;
    }

    protected Person() {
        // FOR JPA
    }

    public String getName() {
        return name;
    }
}
