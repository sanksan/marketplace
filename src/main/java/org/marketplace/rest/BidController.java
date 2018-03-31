package org.marketplace.rest;

import lombok.extern.slf4j.Slf4j;
import org.marketplace.dto.BidDetail;
import org.marketplace.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/bids")
public class BidController {

    @Autowired private BidService bidService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<BidDetail> getBids() {
        return bidService.getBids();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public BidDetail postNewBid(@Valid @RequestBody BidDetail bidDetail) {
        log.debug("postNewBid() - bidDetail="+ bidDetail);
        return bidService.postNewBid(bidDetail);
    }

    @RequestMapping(value = "/{bidid}", method = RequestMethod.PUT)
    public BidDetail updateBid(@PathVariable(value = "bidid") Long bidid,
                               @Valid @RequestBody BidDetail bidDetail) {
        log.debug("updateBid() - bidid="+bidid);
        return bidService.updateBid(bidid, bidDetail);
    }

    @RequestMapping(value = "/{bidid}", method = RequestMethod.GET)
    public BidDetail getBid(@PathVariable(value = "bidid") Long bidid) {
        log.debug("getBid() - bidid="+bidid);
        return bidService.getBid(bidid);
    }

    @RequestMapping(value = "/{bidid}", method = RequestMethod.DELETE)
    public void deleteBid(@PathVariable(value = "bidid") Long bidid) {
        log.debug("deleteBid() - bidid="+bidid);
        bidService.deleteBid(bidid);
    }

}