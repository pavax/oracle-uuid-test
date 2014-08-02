package ch.mimacom.apps.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Person {

//    @Id
//    @GeneratedValue(generator = "PerSeq")
//    @SequenceGenerator(name = "PerSeq", sequenceName = "PERS_SEQ")
//    private Long id;

    @EmbeddedId
    private PersonId personId;

    private String name;

    public Person(String name) {
        this.personId = new PersonId();
        this.name = name;
    }

    protected Person() {
        // FOR JPA
    }

    public String getName() {
        return name;
    }
}
