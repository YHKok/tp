package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));

        // invalid addresses
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only
        assertFalse(Address.isValidAddress("-")); // one character
        assertFalse(Address.isValidAddress("Ubi")); //too short
        assertFalse(Address.isValidAddress("Leng Inc; 1234 Market St; "
                + "San Francisco CA 2349879; USA")); // invalid special characters
        assertFalse(Address.isValidAddress("Leng Incorporation, 123131424456712124152354365635389 Market Street, "
                + "San Francisco CA 234615123625612341245152162513131232131251323621232323232323232323232323124246373, "
                + "UNITED STATES OF AMERICA, SAIKFKAHWIOHTOWEOERWIOENDADASDasdasdsadsadsadasdasdASF")); // long address

        // valid addresses
        assertTrue(Address.isValidAddress("Blk 456, Denver Road, #01-355"));
        assertTrue(Address.isValidAddress("Leng Inc, 1234 Market St., Simei CA 2349879, SG")); // long address
    }

    @Test
    public void equals() {
        Address address = new Address("Leng Inc, 1234 Market St., Simei CA 2349879, SG");

        // same values -> returns true
        assertTrue(address.equals(new Address("Leng Inc, 1234 Market St., Simei CA 2349879, SG")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("Blk 456, Denver Road, #01-355")));
    }
}
