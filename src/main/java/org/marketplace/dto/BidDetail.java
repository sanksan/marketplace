package org.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.marketplace.domain.Bid;
import org.marketplace.domain.Project;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidDetail {

    private Long id;
    private Long projectId;
    private Long userId;
    private String name;
    private String description;
    private BigDecimal bidAmount;
    private ProjectDetail project;

    public static BidDetail toBidDto(Bid bidModel) {
        return BidDetail.builder()
                .id(bidModel.getId())
                .name(bidModel.getName())
                .description(bidModel.getDescription())
                .bidAmount(bidModel.getBidAmount())
                .project(bidModel.getProject() != null ? ProjectDetail.toProjectDtoNoChildren(bidModel.getProject()) : null)
                .build();
    }

    public static BidDetail toBidDtoNoChildren(Bid bidModel) {
        return BidDetail.builder()
                .id(bidModel.getId())
                .name(bidModel.getName())
                .description(bidModel.getDescription())
                .bidAmount(bidModel.getBidAmount())
                .build();
    }

    public static Bid fromBidDto(BidDetail bidDetail) {
        return Bid.builder()
                .name(bidDetail.getName())
                .description(bidDetail.getDescription())
                .bidAmount(bidDetail.getBidAmount())
                .build();
    }
}
