package ch.mimacom.apps.domain;

import org.hibernate.annotations.Type;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Access(AccessType.FIELD)
public class PersonId implements Serializable {

    @Type(type = "org.hibernate.type.UUIDBinaryType")
    @Column(name = "ID", length = 16)
    private UUID value;

    public PersonId(UUID value) {
        this.value = value;
    }

    public PersonId() {
        this.value = IdGenerator.timeBasedUUID();
    }

    public UUID getValue() {
        return value;
    }
}
