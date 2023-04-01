package com.anywhereworks.bookmarks.servicesImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.repositories.BookmarkRepository;
import com.anywhereworks.bookmarks.services.BookmarkService;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceImplTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	private BookmarkService bookmarkService;
	private Bookmark bookmark;

	@BeforeEach
	void setUp() {
		bookmarkService = new BookmarkServiceImpl(bookmarkRepository);
		bookmark = new Bookmark(1, "Java", "/java");

	}

	@Test
	public void shouldAddBookmark() throws BusinessException {
		
		when(bookmarkRepository.save(bookmark)).thenReturn(bookmark);
		assertThat(bookmarkService.addBookmark(bookmark)).isEqualTo(bookmark);
		verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	public void shouldThrowException_WhileAdding_IfGivenNullTitle() {
		Bookmark bookmarkWithNullTitle = new Bookmark();
		assertThatThrownBy(() -> bookmarkService.addBookmark(bookmarkWithNullTitle))
				.isInstanceOf(BusinessException.class).hasMessage("Title cannot be null");
		verify(bookmarkRepository, never()).save(any());
	}

	@Test
	public void shouldThrowException_WhileAdding_IfGivenEmptyTitle() {
		Bookmark bookmarkWithEmptyTitle = new Bookmark();
		bookmarkWithEmptyTitle.setTitle("");
		assertThatThrownBy(() -> bookmarkService.addBookmark(bookmarkWithEmptyTitle))
				.isInstanceOf(BusinessException.class).hasMessage("Title cannot be empty");
		verify(bookmarkRepository, never()).save(any());
	}

	@Test
	public void shouldUpdateBookmark() throws BusinessException {
		when(bookmarkRepository.findById("1")).thenReturn(Optional.ofNullable(bookmark));
		when(bookmarkRepository.save(bookmark)).thenReturn(bookmark);
		assertThat(bookmarkService.updateBookmark("1",bookmark)).isEqualTo(bookmark);
		verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	public void shouldThrowException_WhileUpdating_IfGivenNullTitle() {
		Bookmark bookmarkWithNullTitle = new Bookmark();
		assertThatThrownBy(() -> bookmarkService.updateBookmark("1", bookmarkWithNullTitle))
				.isInstanceOf(BusinessException.class).hasMessage("Title cannot be null");
		verify(bookmarkRepository, never()).save(any());
	}

	@Test
	public void shouldThrowException_WhileUpdating_IfGivenEmptyTitle() {
		Bookmark bookmarkWithEmptyTitle = new Bookmark();
		bookmarkWithEmptyTitle.setTitle("");
		assertThatThrownBy(() -> bookmarkService.updateBookmark("1", bookmarkWithEmptyTitle))
				.isInstanceOf(BusinessException.class).hasMessage("Title cannot be empty");
		verify(bookmarkRepository, never()).save(any());
	}

	@Test
	public void shouldThrowException_WhileUpdating_IfBookmarkNotFound() {
		when(bookmarkRepository.findById("2")).thenReturn(Optional.empty());
		assertThatThrownBy(() -> bookmarkService.updateBookmark("2", bookmark))
				.isInstanceOf(BusinessException.class).hasMessage("Bookmark with id 2 not found");
		verify(bookmarkRepository,times(1)).findById("2");
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	public void shouldGetBookmark() throws BusinessException {
		when(bookmarkRepository.findById("1")).thenReturn(Optional.ofNullable(bookmark));
		assertThat(bookmarkService.getBookmark("1")).isEqualTo(bookmark);
		verify(bookmarkRepository,times(1)).findById("1");
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	public void shouldThrowException_WhileGetting_IfBookmarkNotFound() {
		when(bookmarkRepository.findById("2")).thenReturn(Optional.empty());
		assertThatThrownBy(() -> bookmarkService.getBookmark("2"))
				.isInstanceOf(BusinessException.class).hasMessage("Bookmark with id 2 not found");
		verify(bookmarkRepository,times(1)).findById("2");
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	public void shouldGetAllBookmarks() throws BusinessException {
		when(bookmarkRepository.findAll()).thenReturn(new ArrayList<>(Collections.singleton(bookmark)));
		assertThat(bookmarkService.getAllBookmarks().get(0)).isEqualTo(bookmark);
		verify(bookmarkRepository,times(1)).findAll();
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	public void shouldDeleteBookmark() throws BusinessException {

		when(bookmarkRepository.findById("1")).thenReturn(Optional.ofNullable(bookmark));
		//doNothing().when(bookmarkRepository).deleteById("1");
		bookmarkService.deleteBookmark("1");
		verify(bookmarkRepository,times(1)).deleteById("1");
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	public void shouldThrowException_WhileDeleting_IfBookmarkNotFound() {

		when(bookmarkRepository.findById("2")).thenReturn(Optional.empty());
		assertThatThrownBy(() -> bookmarkService.deleteBookmark("2"))
				.isInstanceOf(BusinessException.class).hasMessage("Bookmark with id 2 doesn't exists");
		verify(bookmarkRepository,times(1)).findById("2");
		verifyNoMoreInteractions(bookmarkRepository);
	}
}
