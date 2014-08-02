package ch.mimacom.apps.domain;

import com.google.common.base.Objects;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Address {

    @NotBlank
    private String street;

    @NotBlank
    private String place;

    @NotBlank
    private String zip;

    public Address(String street, String place, String zip) {
        this.street = street;
        this.place = place;
        this.zip = zip;
    }

    protected Address() {
    }

    public String getStreet() {
        return street;
    }

    public String getPlace() {
        return place;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("street", street)
                .add("place", place)
                .add("zip", zip)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (place != null ? !place.equals(address.place) : address.place != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (zip != null ? !zip.equals(address.zip) : address.zip != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        return result;
    }
}
