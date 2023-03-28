package com.example.TopicProject.services;

import java.util.List;
import java.util.Optional;

import com.example.TopicProject.entities.Topic;

public interface TopicService {
	List<Topic> getAllTopics();
	Optional<Topic> getTopic(String id);
	Optional<Topic> getTopicByName(String name);
	Topic addTopic(Topic topic);
	Topic updateTopic(String id, Topic topic);
	void deleteTopic(String id);
}
