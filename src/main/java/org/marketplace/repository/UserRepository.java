package org.marketplace.repository;

import org.marketplace.domain.Bid;
import org.marketplace.domain.Project;
import org.marketplace.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String userName);
}
