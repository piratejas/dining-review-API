package com.piratejas.diningReviewAPI.repositories;

import com.piratejas.diningReviewAPI.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByName(String name);
}
