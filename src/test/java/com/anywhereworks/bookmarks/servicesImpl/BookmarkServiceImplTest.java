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
import org.junit.jupiter.api.DisplayName;
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
	@InjectMocks
	private BookmarkServiceImpl bookmarkServiceImpl;

	private static final Bookmark bookmark = new Bookmark(1, "Java", "/java");;

	@Test
	@DisplayName("Add the bookmark")
	public void shouldAddBookmark() throws BusinessException {
		// when
		when(bookmarkRepository.save(bookmark)).thenReturn(bookmark);
		
		// then
		assertThat(bookmarkServiceImpl.addBookmark(bookmark)).isEqualTo(bookmark);
		verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	@DisplayName("Throw exception while adding if given null or blank title")
	public void shouldThrowExceptionWhileAddingIfGivenInvalidTitle() {
		// given
		Bookmark bookmarkWithInvalidTitle = new Bookmark();

		// when-then
		assertThatThrownBy(() -> bookmarkServiceImpl.addBookmark(bookmarkWithInvalidTitle))
				.isInstanceOf(BusinessException.class).hasMessage("Title is invalid");
		verify(bookmarkRepository, never()).save(any());
	}

	@Test
	@DisplayName("Update the bookmark with given id")
	public void shouldUpdateBookmark() throws BusinessException {
		// given
		long bookmarkId = bookmark.getBookmarkId();

		// when
		when(bookmarkRepository.findById(1L)).thenReturn(Optional.ofNullable(bookmark));
		when(bookmarkRepository.save(bookmark)).thenReturn(bookmark);
		assertThat(bookmarkServiceImpl.updateBookmark(1L, bookmark)).isEqualTo(bookmark);

		// then
		verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	@DisplayName("Throw exception while updating if given null or blank title")
	public void shouldThrowExceptionWhileUpdatingIfGivenInvalidTitle() {
		// given
		Bookmark bookmarkWithInvalidTitle = new Bookmark();

		// when
		assertThatThrownBy(() -> bookmarkServiceImpl.updateBookmark(1L, bookmarkWithInvalidTitle))
				.isInstanceOf(BusinessException.class).hasMessage("Title is invalid");

		// then
		verify(bookmarkRepository, never()).save(any());
	}

	@Test
	@DisplayName("Throw exception while updating if bookmark with given id not found")
	public void shouldThrowExceptionWhileUpdatingIfBookmarkNotFound() {
		// given
		long bookmarkId = 2L;

		// when
		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> bookmarkServiceImpl.updateBookmark(bookmarkId, bookmark))
				.isInstanceOf(BusinessException.class).hasMessage("Bookmark with id " + bookmarkId + " not found");

		// then
		verify(bookmarkRepository, times(1)).findById(2L);
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	@DisplayName("Get the bookmark with given id")
	public void shouldGetBookmark() throws BusinessException {
		// given
		long bookmarkId = bookmark.getBookmarkId();

		// when
		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.ofNullable(bookmark));
		assertThat(bookmarkServiceImpl.getBookmark(bookmarkId)).isEqualTo(bookmark);

		// then
		verify(bookmarkRepository, times(1)).findById(bookmarkId);
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	@DisplayName("Throw exception while geting if bookmark with given id not found")
	public void shouldThrowExceptionWhileGettingIfBookmarkNotFound() {
		// given
		long bookmarkId = 2L;

		// when
		when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> bookmarkServiceImpl.getBookmark(bookmarkId)).isInstanceOf(BusinessException.class)
				.hasMessage("Bookmark with id " + bookmarkId + " not found");

		// then
		verify(bookmarkRepository, times(1)).findById(bookmarkId);
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	@DisplayName("Get all the bookmarks")
	public void shouldGetAllBookmarks() throws BusinessException {
		//when
		when(bookmarkRepository.findAll()).thenReturn(new ArrayList<>(Collections.singleton(bookmark)));
		assertThat(bookmarkServiceImpl.getAllBookmarks().get(0)).isEqualTo(bookmark);
		
		// then
		verify(bookmarkRepository,times(1)).findAll();
		verifyNoMoreInteractions(bookmarkRepository);
	}

	@Test
	@DisplayName("Delete the bookmark with given id")
	public void shouldDeleteBookmark() throws BusinessException {

		// stub
		// doNothing().when(bookmarkRepository).deleteById("1");

		// given
		long bookmarkId = 1L;

		// when
		bookmarkServiceImpl.deleteBookmark(bookmarkId);

		// then
		verify(bookmarkRepository, times(1)).deleteById(bookmarkId);
		verifyNoMoreInteractions(bookmarkRepository);
	}

}
