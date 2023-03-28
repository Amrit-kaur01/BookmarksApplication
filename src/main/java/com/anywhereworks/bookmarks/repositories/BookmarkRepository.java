package com.example.TopicProject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.TopicProject.entities.Topic;

public interface TopicRepository extends CrudRepository<Topic, String> {

	/*
	 * the framework sees the embedded Derby database in the classpath and assumes
	 * that to be the database to connect to. No connection info necessary.
	 */
	@Query("select t from Topic t where t.name = ?1")
	Optional<Topic> findByName(String name);
}
