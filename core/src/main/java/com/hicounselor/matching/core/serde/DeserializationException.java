package com.hicounselor.matching.core.serde;

public class DeserializationException extends RuntimeException {

    public DeserializationException(final Exception e) {
        super(e);
    }

}
