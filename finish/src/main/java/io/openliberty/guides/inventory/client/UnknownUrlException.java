// tag::exception[]
package io.openliberty.guides.inventory.client;

public class UnknownUrlException extends Exception {

    public UnknownUrlException() {
        super();
    }

    /**
     * @param message
     */
    public UnknownUrlException(String message) {
        super(message);
    }
}
// end::exception[]
