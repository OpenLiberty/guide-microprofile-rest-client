// tag::exception[]
package io.openliberty.guides.inventory.client;

public class UnknownHostNameException extends Exception {

    public UnknownHostNameException() {
        super();
    }

    /**
     * @param message
     */
    public UnknownHostNameException(String message) {
        super(message);
    }
}
// end::exception[]
