package com.anywhereworks.bookmarks.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.hamcrest.Matchers.*;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.repositories.BookmarkRepository;
import com.anywhereworks.bookmarks.services.BookmarkService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	BookmarkService bookmarkService;

	Bookmark bookmark1 = new Bookmark(1, "java", "/java");
	Bookmark bookmark2 = new Bookmark(2, "python", "/python");

	@Test
	public void shouldGetAllBookmarks() throws Exception {
		List<Bookmark> bookmarksList = new ArrayList<>(Arrays.asList(bookmark1, bookmark2));
		when(bookmarkService.getAllBookmarks()).thenReturn(bookmarksList);

		mockMvc.perform(MockMvcRequestBuilders.get("/bookmarks").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].title", is(bookmark1.getTitle())));

	}

	@Test
	public void shouldGetBookmark() throws Exception {
		when(bookmarkService.getBookmark("1")).thenReturn(bookmark1);
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmarks/1").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",notNullValue()))
			.andExpect(jsonPath("$.title",is("java")));
	}

	@Test
	public void shouldAddBookmark() throws Exception {
		Bookmark bookmarkToAdd = new Bookmark(3, "c++", "/c++");
		when(bookmarkService.addBookmark(bookmarkToAdd)).thenReturn(bookmarkToAdd);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/bookmarks")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bookmarkToAdd));

		mockMvc.perform(mockRequest).andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("c++"))).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void shouldUpdateBookmark() throws Exception {
		Bookmark updatedBookmark1 = new Bookmark(1, "Advanced java", "/java");
		when(bookmarkService.updateBookmark("1", updatedBookmark1)).thenReturn(updatedBookmark1);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/bookmarks/1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBookmark1));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Advanced java")));
	}

	@Test
	public void shouldDeleteBookmark() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/bookmarks/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", is("Bookmark with id 1 deleted successfully")));
	}

}
