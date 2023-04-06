package com.anywhereworks.bookmarks.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.services.FolderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FolderController.class)
class FolderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private FolderService folderService;

	private static final Folder folder1 = new Folder(1L, null, "Folder 1");

	@Test
	@DisplayName("Should get the folder with given id")
	public void shouldGetFolder() throws Exception {
		// given
		long folderId = 1L;

		// when
		when(folderService.getFolder(folderId)).thenReturn(folder1);

		// then
		mockMvc.perform(MockMvcRequestBuilders.get("/folders/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("Folder 1")));
	}

	@Test
	@DisplayName("Should add the folder")
	public void shouldAddFolder() throws Exception {
		// given
		Folder folderToAdd = new Folder(2l, null, "New Folder");

		// when
		when(folderService.addFolder(folderToAdd)).thenReturn(folderToAdd);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/folders")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(folderToAdd));

		// then
		mockMvc.perform(mockRequest).andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is(folderToAdd.getName())));
	}

	@Test
	@DisplayName("Should update the folder with given id")
	public void shouldUpdateFolder() throws Exception {
		// given
		long folderId = 2L;
		Folder updatedFolder = new Folder(2L, null, "New folder updated");

		// when
		when(folderService.updateFolder(folderId, updatedFolder)).thenReturn(updatedFolder);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/folders/{folderId}", folderId)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedFolder));

		// then
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is(updatedFolder.getName()))).andDo(print());
	}

	@Test
	@DisplayName("Should delete the folder with the given id")
	public void shouldDeleteFolder() throws Exception {
		// given
		long folderId = 1L;

		// when-then
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/folders/{folderId}", folderId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", is("Folder with id 1 deleted")));
	}

}
