package org.marketplace.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"bids", "projects"})
@ToString(exclude = {"bids", "projects"})
@Getter
@Setter
@Entity
@Table(name = "USER")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USR_ID",unique = true, nullable = false)
    private Long id;
    @Column(name="USR_NM")  private String username;
    @Column(name="API_KEY")  private String apiKey;
    @Column(name="USR_DSPNM")  private String displayName;
    @Column(name="USR_DESC")  private String description;
    @Column(name="USR_RPTN")  private String reputation;
    @Column(name="USR_JND_TS")  private Instant joinedDate;
    @Column(name="USR_LTST_TS")  private Instant lastLogonDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<Bid> bids;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<Project> projects;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CRTD_DT")
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDTD_DT")
    private Date modifiedDate;

    @Version
    @Column(name="OPTMST_LCK", nullable = false, columnDefinition = "integer DEFAULT 0")
    private Long optimLock = 0L;

    @PrePersist
    public void generateApiKey() {
        if(StringUtils.isEmpty(apiKey))
            apiKey =  UUID.randomUUID().toString();
    }

}
