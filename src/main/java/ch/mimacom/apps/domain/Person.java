package ch.mimacom.apps.domain;

import com.google.common.base.Objects;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {

//    @Id
//    @GeneratedValue(generator = "PerSeq")
//    @SequenceGenerator(name = "PerSeq", sequenceName = "PERS_SEQ")
//    private Long id;

    @EmbeddedId
    private PersonId personId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(
            joinColumns = @JoinColumn(name = "PERSON_ID")
    )
    private Set<Address> addresses = new HashSet<Address>();

    public Person(String firstName, String lastName, Address... address) {
        this.lastName = lastName;
        this.personId = new PersonId();
        this.firstName = firstName;
        this.createdAt = LocalDateTime.now();
        this.addresses.addAll(Arrays.asList(address));
    }

    protected Person() {
        // FOR JPA
    }

    public String getFirstName() {
        return firstName;
    }


    public PersonId getPersonId() {
        return personId;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<Address> getAddresses() {
        return Collections.unmodifiableSet(addresses);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("personId", personId)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("createdAt", createdAt)
                .add("addresses", addresses)
                .toString();
    }
}
