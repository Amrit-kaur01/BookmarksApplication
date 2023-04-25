package com.anywhereworks.bookmarks.controllers;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.hamcrest.Matchers.*;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.services.BookmarkService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BookmarkService bookmarkService;

	private static final Bookmark bookmark1 = new Bookmark(1, "java", "/java");
	private static final Bookmark bookmark2 = new Bookmark(2, "python", "/python");

	@Test
	@DisplayName("Should get all bookmarks")
	public void shouldGetAllBookmarks() throws Exception {
		// given
		List<Bookmark> bookmarksList = new ArrayList<>(Arrays.asList(bookmark1, bookmark2));

		// when
		when(bookmarkService.getAllBookmarks()).thenReturn(bookmarksList);

		// then
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmarks").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].title", is(bookmark1.getTitle())));

	}

	@Test
	@DisplayName("Should get the bookmark with given id")
	public void shouldGetBookmark() throws Exception {
		// given
		long bookmarkId = 1L;

		// when
		when(bookmarkService.getBookmark(bookmarkId)).thenReturn(bookmark1);

		// then
		mockMvc.perform(MockMvcRequestBuilders.get("/bookmarks/{bookmarkId}", bookmarkId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.title", is("java")));
	}

	@Test
	@DisplayName("Should add the bookmark")
	public void shouldAddBookmark() throws Exception {
		// given
		Bookmark bookmarkToAdd = new Bookmark(3, "c++", "/c++");

		// when
		when(bookmarkService.addBookmark(bookmarkToAdd)).thenReturn(bookmarkToAdd);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/bookmarks")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookmarkToAdd));

		// then
		mockMvc.perform(mockRequest).andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("c++"))).andDo(print());
	}

	@Test
	@DisplayName("Should update the bookmark with given id")
	public void shouldUpdateBookmark() throws Exception {
		// given
		long bookmarkId = 1L;
		Bookmark updatedBookmark = new Bookmark(1L, "Advanced java", "/java");

		// when
		when(bookmarkService.updateBookmark(bookmarkId, updatedBookmark)).thenReturn(updatedBookmark);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/bookmarks/{bookmarkId}", bookmarkId)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBookmark));

		// then
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.title", is("Advanced java"))).andDo(print());
	}

	@Test
	@DisplayName("Should delete the bookmark with given id")
	public void shouldDeleteBookmark() throws Exception {
		// given
		long bookmarkId = 1L;

		// when-then
		mockMvc.perform(MockMvcRequestBuilders.delete("/bookmarks/{bookmarkId}", bookmarkId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", is("Bookmark with id " + bookmarkId + " deleted")));
	}

}
