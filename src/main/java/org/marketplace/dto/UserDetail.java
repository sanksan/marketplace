package org.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.marketplace.domain.User;

import javax.validation.constraints.NotNull;
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
public class UserDetail {

    private Long id;
    @NotNull(message = "User name cannot be empty")
    private String username;
    private String apiKey;
    @NotNull(message = "User display name cannot be empty")
    private String displayName;
    private String description;
    private String reputation;
    private Instant joinedDate;
    private Instant lastLogonDate;
    private Set<BidDetail> bidDetails;
    private Set<ProjectDetail> projectDetails;

    public static UserDetail toUserDto(User userModel) {
        Set<BidDetail> bidDetails = null;
        Set<ProjectDetail> projectDetails = null;
        if(userModel != null && userModel.getBids() != null)
            bidDetails = userModel.getBids().stream().map(BidDetail::toBidDtoNoChildren).collect(Collectors.toSet());
        if(userModel != null && userModel.getProjects() != null)
            projectDetails = userModel.getProjects().stream().map(ProjectDetail::toProjectDtoNoChildren).collect(Collectors.toSet());
        return UserDetail.builder()
                .id(userModel.getId())
                .username(userModel.getUsername())
                .apiKey(userModel.getApiKey())
                .displayName(userModel.getDisplayName())
                .description(userModel.getDescription())
                .reputation(userModel.getReputation())
                .bidDetails(bidDetails)
                .projectDetails(projectDetails)
                .build();
    }


    public static User fromUserDto(UserDetail userDetail) {
        return User.builder()
                .id(userDetail.getId())
                .username(userDetail.getUsername())
                .displayName(userDetail.getDisplayName())
                .description(userDetail.getDescription())
                .reputation(userDetail.getReputation())
                .build();
    }
}
