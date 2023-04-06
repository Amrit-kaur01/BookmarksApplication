package com.anywhereworks.bookmarks.servicesImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.repositories.BookmarkRepository;
import com.anywhereworks.bookmarks.repositories.FolderRepository;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

	@Mock
	private FolderRepository folderRepository;

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private FolderServiceImpl folderServiceImpl;

	private static final Bookmark bookmark1 = new Bookmark(1, "java", "/java");
	private static final Set<Bookmark> bookmarksSet = new HashSet<>(Set.of(bookmark1));
	private Folder folder = new Folder(1, bookmarksSet, "Folder1");

	@Test
	@DisplayName("Add the folder") 
	public void shouldAddFolder() throws BusinessException {
		// when
		when(folderRepository.save(folder)).thenReturn(folder);
		assertThat(folderServiceImpl.addFolder(folder)).isEqualTo(folder);
		
		// then
		verify(folderRepository,times(1)).save(folder);
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	@DisplayName("Throw exception while adding if given null or blank name")
	public void shouldThrowExceptionWhileAddingIfGivenInvalidName() {
		// given
		Folder folderWithInvalidName = new Folder();

		// when
		assertThatThrownBy(() -> folderServiceImpl.addFolder(folderWithInvalidName))
				.isInstanceOf(BusinessException.class).hasMessage("Name is invalid");

		// then
		verify(folderRepository, never()).save(any());
	}

	@Test
	@DisplayName("Update the folder with given id")
	public void shouldUpdateFolder() throws BusinessException {
		// given
		long folderId = 1L;
		String updatedName = "Folder1updated";
		folder.setName(updatedName);

		// when
		when(folderRepository.findById(folderId)).thenReturn(Optional.ofNullable(folder));
		when(folderRepository.save(folder)).thenReturn(folder);
		assertThat(folderServiceImpl.updateFolder(folderId, folder).getName()).isEqualTo(updatedName);

		// then
		verify(folderRepository, times(1)).save(any());
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	@DisplayName("Throw exception while updating if given null or blank name")
	public void shouldThrowExceptionWhileUpdatingIfGivenInvalidName() {
		// given
		Folder folderWithInvalidName = new Folder();

		// when
		assertThatThrownBy(() -> folderServiceImpl.updateFolder(1L, folderWithInvalidName))
				.isInstanceOf(BusinessException.class).hasMessage("Name is invalid");

		// then
		verify(folderRepository, never()).save(any());
	}

	@Test
	@DisplayName("Throw exception while updating if folder with given id not found")
	public void shouldThrowExceptionWhileUpdatingIfFolderNotFound() {
		// given
		long folderId = 2L;

		// when
		when(folderRepository.findById(folderId)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> folderServiceImpl.updateFolder(folderId, folder)).isInstanceOf(BusinessException.class)
				.hasMessage("Folder with id " + folderId + " not found");

		// then
		verify(folderRepository, times(1)).findById(folderId);
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	@DisplayName("Delete the folder with given id")
	public void shouldDeleteFolder() throws BusinessException {
		// given
		long folderId = 1L;

		// when
		folderServiceImpl.deleteFolder(folderId);

		// then
		verify(folderRepository, times(1)).deleteById(folderId);
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	@DisplayName("Get the folder with given id")
	public void shouldGetFolder() throws BusinessException {
		// given
		long folderId = 1L;

		// when
		when(folderRepository.findById(folderId)).thenReturn(Optional.ofNullable(folder));
		assertThat(folderServiceImpl.getFolder(folderId)).isEqualTo(folder);

		// then
		verify(folderRepository, times(1)).findById(folderId);
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	@DisplayName("Throw exception while geting if folder with given id not found")
	public void shouldThrowExceptionWhileGettingIfBookmarkNotFound() {
		// given
		long folderId = 2L;

		// when
		when(folderRepository.findById(folderId)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> folderServiceImpl.getFolder(folderId)).isInstanceOf(BusinessException.class)
				.hasMessage("Folder with id " + folderId + " not found");

		// then
		verify(folderRepository, times(1)).findById(folderId);
		verifyNoMoreInteractions(folderRepository);
	}

}
