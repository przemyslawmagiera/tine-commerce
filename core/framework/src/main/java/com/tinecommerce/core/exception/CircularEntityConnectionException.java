package com.tinecommerce.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CircularEntityConnectionException extends Exception {
    public CircularEntityConnectionException(String s) {
        super(s);
    }
}
