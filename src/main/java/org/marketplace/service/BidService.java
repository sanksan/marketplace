package org.marketplace.service;

import lombok.extern.slf4j.Slf4j;
import org.marketplace.domain.Bid;
import org.marketplace.domain.Project;
import org.marketplace.domain.User;
import org.marketplace.dto.BidDetail;
import org.marketplace.exception.NotFoundException;
import org.marketplace.repository.BidRepository;
import org.marketplace.repository.ProjectRepository;
import org.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class BidService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BidRepository bidRepository;

    public List<BidDetail> getBids() {
        Iterable<Bid> bidIterable = bidRepository.findAll();
        return StreamSupport.stream(bidIterable.spliterator(), false)
                .map(BidDetail::toBidDto)
                .collect(Collectors.toList());
    }

    public BidDetail postNewBid(BidDetail bidDetail) {
        log.debug("postNewBid() - bidDetail="+ bidDetail);

        Optional<Project> project = projectRepository.findById(bidDetail.getProjectId());
        if(!project.isPresent())
            throw new NotFoundException("Invalid project id "+ bidDetail.getProjectId());
        Optional<User> user = userRepository.findById(bidDetail.getUserId());
        if(!user.isPresent())
            throw new NotFoundException("Invalid user id "+ bidDetail.getUserId());
        Bid bidModel = BidDetail.fromBidDto(bidDetail);
        Project projectModel = project.get();
        BigDecimal projectLowBidAmt = projectModel.getLowestBid() != null ? projectModel.getLowestBid().getBidAmount() : new BigDecimal(-1);
        if((projectLowBidAmt.intValue() == -1)
                || (bidDetail.getBidAmount() != null
                    && bidDetail.getBidAmount().compareTo(projectLowBidAmt) < 0)) {
            projectModel.setLowestBid(bidModel);
            bidModel.setParentProject(projectModel);
        }
        bidModel.setProject(projectModel);
        User userModel = user.get();
        bidModel.setUser(userModel);
        bidRepository.save(bidModel);
        projectModel.getBids().add(bidModel);
        projectRepository.save(projectModel);

        return BidDetail.toBidDto(bidModel);//bidRepository.findById(bidModel.getId()).get());
    }

    public BidDetail updateBid(Long bidId, BidDetail bidDetail) {
        log.debug("updateBid() - bidId="+bidId);
        Optional<Bid> bid = bidRepository.findById(bidId);
        if(bid.isPresent()) {
            Bid bidModel = bid.get();
            bidModel.setName(bidDetail.getName());
            bidModel.setDescription(bidDetail.getDescription());
            bidModel.setBidAmount(bidDetail.getBidAmount());
            Bid persistentBid = bidRepository.save(bidModel);
            if(bidModel.getProject() != null
                    && bidModel.getProject().getLowestBid() != null
                    && bidModel.getProject().getLowestBid().equals(bidModel)) {
                updateLowestBid(bidModel.getProject());
                projectRepository.save(bidModel.getProject());
            }
            return BidDetail.toBidDto(persistentBid);
        } else
            throw new NotFoundException(String.format("Bid with id %s doesn't exist", bidId));
    }

    public BidDetail getBid(Long bidId) {
        log.debug("getBid() - bidId="+bidId);
        Optional<Bid> optBid = bidRepository.findById(bidId);
        if(optBid.isPresent())
            return BidDetail.toBidDto(optBid.get());
        else
            throw new NotFoundException(String.format("Bid with id %s doesn't exist", bidId));
    }

    public void deleteBid(Long bidId) {
        log.debug("deleteBid() - bidId="+bidId);
        Optional<Bid> optBid = bidRepository.findById(bidId);
        if(!optBid.isPresent())
            throw new NotFoundException(String.format("Bid with id %s doesn't exist", bidId));
        Bid bid = optBid.get();
        if(bid != null) {
            Project project = bid.getProject();
            project.getBids().remove(bid);
            bid.setProject(null);

            if(project != null
                    && project.getLowestBid() != null
                    && project.getLowestBid().equals(bid)) {
                bid.setParentProject(null);
                updateLowestBid(project);
            }
            projectRepository.save(project);
        }
    }

    public Bid updateLowestBid(Project projectModel) {
        Bid lowestBid = null;
        if(projectModel != null && projectModel.getBids() != null) {
            lowestBid = projectModel.getBids().stream().min(Comparator.comparing(Bid::getBidAmount)).orElse(null);
            projectModel.setLowestBid(lowestBid);
//            projectRepository.save(projectModel);
        }
        return lowestBid;
    }

}