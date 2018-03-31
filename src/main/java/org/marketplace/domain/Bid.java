package org.marketplace.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "project")
@Getter
@Setter
@Entity
@Table(name = "BID")
public class Bid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BID_ID",unique = true, nullable = false)
    private Long id;
    @Column(name="BID_NM")  private String name;
    @Column(name="BID_DESC")  private String description;
    @Column(name="BID_AMT")  private BigDecimal bidAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USR_ID", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PJT_ID", nullable=false)
    private Project project;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "lowestBid")
    private Project parentProject;

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

    @Override
    public String toString() {
        return "Project=" + id + ", " + name;
    }


}
