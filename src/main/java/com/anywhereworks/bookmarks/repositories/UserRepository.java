package com.anywhereworks.bookmarks.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.anywhereworks.bookmarks.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByUsername(String username);
}
