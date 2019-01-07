package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

/**
 * Enum representing result of trying to fetch a book from the inventory.
 */
public enum OrderResult implements Serializable {

    NOT_IN_STOCK,
    SUCCESSFULLY_TAKEN,

}
