package org.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.marketplace.domain.Bid;
import org.marketplace.domain.Project;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDetail {

    private Long id;

    @NotNull(message = "User id cannot be empty")
    private Long userId;

    @NotNull(message = "project name cannot be empty")
    private String name;

    @NotNull(message = "project description cannot be empty")
    private String description;

    @Min(0)
    private BigDecimal maxAmount;

    @NotNull(message = "Project start date is empty")
    @FutureOrPresent(message = "Project start date cannot be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant startDate;

    @NotNull(message = "Project end date is empty")
    @FutureOrPresent(message = "Project end date cannot be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant deadline;

    private String status;
    private BidDetail lowestBid;

    private BigDecimal minBidAmt;

    private Set<BidDetail> bids;

    public static ProjectDetail toProjectDto(Project projectModel) {

        Set<BidDetail> bidDetails = null;
        if(projectModel != null && projectModel.getBids() != null)
            bidDetails = projectModel.getBids().stream().map(BidDetail::toBidDtoNoChildren).collect(Collectors.toSet());
        return ProjectDetail.builder()
                .id(projectModel.getId())
                .name(projectModel.getName())
                .description(projectModel.getDescription())
                .maxAmount(projectModel.getMaxAmount())
                .startDate(projectModel.getStartDate())
                .deadline(projectModel.getDeadline())
                .status(projectModel.getProjectStatus() != null ? projectModel.getProjectStatus().toString() : null)
                .minBidAmt(projectModel.getLowestBid() != null ? projectModel.getLowestBid().getBidAmount() : null)
                .lowestBid(projectModel.getLowestBid() != null ? BidDetail.toBidDtoNoChildren(projectModel.getLowestBid()) : null)
                .bids(bidDetails)
                .build();
    }

    public static ProjectDetail toProjectDtoNoChildren(Project projectModel) {

        return ProjectDetail.builder()
                .id(projectModel.getId())
                .name(projectModel.getName())
                .description(projectModel.getDescription())
                .maxAmount(projectModel.getMaxAmount())
                .startDate(projectModel.getStartDate())
                .deadline(projectModel.getDeadline())
                .status(projectModel.getProjectStatus() != null ? projectModel.getProjectStatus().toString() : null)
                .build();
    }


    public static Project fromProjectDto(ProjectDetail projectDetail) {
        return Project.builder()
                .id(projectDetail.getId())
                .name(projectDetail.getName())
                .description(projectDetail.getDescription())
                .maxAmount(projectDetail.getMaxAmount())
                .startDate(projectDetail.getStartDate())
                .deadline(projectDetail.getDeadline())
                .status(projectDetail.getStatus())
                .build();
    }
}
