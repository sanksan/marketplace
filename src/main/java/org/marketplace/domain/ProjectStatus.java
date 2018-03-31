package org.marketplace.domain;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public enum ProjectStatus {
    OPEN,
    BID_COMPLETED,
    COMPLETED

}
