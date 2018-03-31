package org.marketplace.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"bids", "user", "lowestBid"})
@ToString(exclude = {"bids", "user", "lowestBid"})
@Getter
@Setter
@Entity
@Table(name = "PROJECT")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PJT_ID",unique = true, nullable = false)
    private Long id;
    @Column(name="PJT_NM")  private String name;
    @Column(name="PJT_DESC")  private String description;
    @Column(name="PJT_MAXAMT")  private BigDecimal maxAmount;
    @Column(name="PJT_START_TS")  private Instant startDate;
    @Column(name="PJT_END_TS")  private Instant deadline;
    @Column(name="PJT_STTS")  private String status;

    @Enumerated(EnumType.STRING)
    @Formula("case when PJT_END_TS > now() then 'OPEN' else 'BID_COMPLETED' end")
    private ProjectStatus projectStatus = ProjectStatus.OPEN;

    @OneToOne
    @JoinColumn(name = "LWST_BID_ID")
    private Bid lowestBid;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private Set<Bid> bids;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USR_ID", nullable=false)
    private User user;

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
}
