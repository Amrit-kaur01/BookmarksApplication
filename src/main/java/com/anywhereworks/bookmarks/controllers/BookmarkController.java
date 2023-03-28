package com.example.TopicProject.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.TopicProject.entities.Topic;
import com.example.TopicProject.services.TopicServiceImpl;

@RestController
public class TopicController {

	@Autowired // used to declare a dependency that spring needs to inject at runtime
	private TopicServiceImpl topicServiceImpl;

	@GetMapping("/topics")
	public ResponseEntity<Object> getAllTopics() { // whatever this method returns will automatically be converted to JSON and sent
										// back as the HTTPResponse since this is a RestController
		List<Topic> list = topicServiceImpl.getAllTopics();
		if(list.size()<=0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No topics found");
		}
		return ResponseEntity.of(Optional.of(list));
		/*
		 * the generated JSON will have key names corresponding to property names of the
		 * Topic class The JSON values are the values of those properties
		 */
	}

	@GetMapping("/topics/{id}") // the id inside the curly brackets indicates that it is a variable token and it
									// can take any value and that value will be passed to the method
	public ResponseEntity<?> getTopic(@PathVariable String id) {
		Optional<Topic> topic= topicServiceImpl.getTopic(id);
		if(!topic.isEmpty())
			return ResponseEntity.of(topic);   
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topic with id "+id+" doesn't exists.");
	}
	
	@GetMapping("/topics/name/{name}")
	public ResponseEntity<?> getTopicByName(@PathVariable String name) {
		Optional<Topic> topic = this.topicServiceImpl.getTopicByName(name);
		if(topic.isPresent())
			return ResponseEntity.of(topic);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topic with name "+name+" doesn't exists.");

	}

	@PostMapping("/topics")
	public ResponseEntity<Topic> addTopic(@RequestBody Topic topic) // @RequestBody converts or maps the HTTPRequest body into a Topic
													// instance, enabling automatic deserialization
	{
		try {
			Topic topicUpdated = topicServiceImpl.addTopic(topic);
			return ResponseEntity.status(HttpStatus.CREATED).body(topicUpdated);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}

	@PutMapping("/topics/{id}")
	public ResponseEntity<Topic> updateTopic(@RequestBody Topic topic, @PathVariable String id) {
		try {
			Topic topicUpdated = this.topicServiceImpl.updateTopic(id, topic);
			return ResponseEntity.ok().body(topicUpdated);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/topics/{id}")
	public ResponseEntity<Object> deleteTopic(@PathVariable String id) {
		try {
			this.topicServiceImpl.deleteTopic(id);
			return ResponseEntity.status(HttpStatus.OK).body("Topic with id "+id+" deleted successfully");
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
