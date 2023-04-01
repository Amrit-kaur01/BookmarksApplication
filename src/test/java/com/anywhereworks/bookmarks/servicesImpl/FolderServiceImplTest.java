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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.repositories.FolderRepository;
import com.anywhereworks.bookmarks.services.FolderService;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

	@Mock
	private FolderRepository folderRepository;

	private FolderService folderService;
	private Folder folder;
	private Set<Bookmark> bookmarksSet;
	private Bookmark bookmark1;
	private Bookmark bookmark2;

	@BeforeEach
	void setUp() throws Exception {
		folderService = new FolderServiceImpl(folderRepository);
		bookmark1 = new Bookmark(1, "java", "/java");
		bookmark2 = new Bookmark(2, "js", "/js");
		bookmarksSet = new HashSet<>(Set.of(bookmark1));
		folder = new Folder(1, bookmarksSet, "Folder1");
	}

	@Test
	public void shouldAddFolder() throws BusinessException {
		when(folderRepository.save(folder)).thenReturn(folder);
		assertThat(folderService.addFolder(folder)).isEqualTo(folder);
		verify(folderRepository,times(1)).save(folder);
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	public void shouldThrowException_WhileAdding_IfGivenNullName() {
		Folder folderWithNullName = new Folder();
		assertThatThrownBy(() -> folderService.addFolder(folderWithNullName)).isInstanceOf(BusinessException.class)
				.hasMessage("Name cannot be null");
		verify(folderRepository, never()).save(any());
	}

	@Test
	public void shouldThrowException_WhileAdding_IfGivenBlankName() {
		Folder folderWithBlankName = new Folder();
		folderWithBlankName.setName(" ");
		assertThatThrownBy(() -> folderService.addFolder(folderWithBlankName)).isInstanceOf(BusinessException.class)
				.hasMessage("Name cannot be blank");
		verify(folderRepository, never()).save(any());
	}

	@Test
	public void shouldUpdateBookmark() throws BusinessException {
		String updatedName = "Folder1updated";
		folder.setName(updatedName);
		when(folderRepository.findById("1")).thenReturn(Optional.ofNullable(folder));
		when(folderRepository.save(folder)).thenReturn(folder);
		assertThat(folderService.updateFolder("1", folder).getName()).isEqualTo(updatedName);
		verify(folderRepository, times(1)).save(any());
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	public void shouldThrowException_WhileUpdating_IfGivenNullName() {
		Folder folderkWithNullName = new Folder();
		assertThatThrownBy(() -> folderService.updateFolder("1", folderkWithNullName))
				.isInstanceOf(BusinessException.class).hasMessage("Name cannot be null");
		verify(folderRepository, never()).save(any());
	}

	@Test
	public void shouldThrowException_WhileUpdating_IfGivenBlankName() {
		Folder folderWithBlankName = new Folder();
		folderWithBlankName.setName("");
		assertThatThrownBy(() -> folderService.updateFolder("1", folderWithBlankName))
				.isInstanceOf(BusinessException.class).hasMessage("Name cannot be blank");
		verify(folderRepository, never()).save(any());
	}

	@Test
	public void shouldThrowException_WhileUpdating_IfFolderNotFound() {
		when(folderRepository.findById("2")).thenReturn(Optional.empty());
		assertThatThrownBy(() -> folderService.updateFolder("2", folder))
				.isInstanceOf(BusinessException.class).hasMessage("Folder with id 2 not found");
		verify(folderRepository,times(1)).findById("2");
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	public void shouldDeleteFolder() throws BusinessException {
		when(folderRepository.existsById("1")).thenReturn(true);
		folderService.deleteFolder("1");
		verify(folderRepository,times(1)).deleteById("1");
		verifyNoMoreInteractions(folderRepository);
	}

	@Test
	public void shouldThrowException_WhileDeleting_IfFolderNotFound() {
		when(folderRepository.existsById("2")).thenReturn(false);
		assertThatThrownBy(() -> folderService.deleteFolder("2"))
				.isInstanceOf(BusinessException.class).hasMessage("Folder with id 2 doesn't exists");
		verify(folderRepository,never()).deleteById(any());
	}

}
