package org.marketplace.exception;

import java.util.Date;
import java.util.UUID;

public class ErrorDetails {
    private UUID traceId;
    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.traceId = UUID.randomUUID();
    }
}
