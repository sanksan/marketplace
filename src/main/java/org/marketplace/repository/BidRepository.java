package org.marketplace.repository;

import org.marketplace.domain.Bid;
import org.marketplace.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends CrudRepository<Bid, Long> {

    List<Bid> findByName(String bidName);
}
