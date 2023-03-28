package com.example.TopicProject.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TopicProject.entities.Topic;
import com.example.TopicProject.exception.custom.BusinessException;
import com.example.TopicProject.repositories.TopicRepository;

@Service
public class TopicServiceImpl implements TopicService { // this class will provide business service to the controller.
														// Then controller
	// may give the response to the url request made by the user

	@Autowired
	private TopicRepository topicRepository;

	@Override
	public List<Topic> getAllTopics() {
		List<Topic> topics = new ArrayList<>();
		topicRepository.findAll().forEach(topics::add);
		return topics;
	}

	@Override
	public Optional<Topic> getTopic(String id) {
		return topicRepository.findById(id);
	}

	@Override
	public Topic addTopic(Topic topic) {
		return topicRepository.save(topic);

	}

	@Override
	public Topic updateTopic(String id, Topic topic) {
		return topicRepository.save(topic);
		/*
		 * save() method can do both add and update an instance that's why using same
		 * method in both .this is because the sent instance has the complete
		 * information or data hence the repository will check if there is already a row
		 * with same id. If it is so, then it will update the row. Otherwise add a new
		 * row i.e insert
		 */
	}

	@Override
	public void deleteTopic(String id) {
		topicRepository.deleteById(id);
	}

	@Override
	public Optional<Topic> getTopicByName(String name) {

		return topicRepository.findByName(name);
	}
}
