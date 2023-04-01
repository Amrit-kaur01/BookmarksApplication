package com.anywhereworks.bookmarks.servicesImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.repositories.BookmarkRepository;
import com.anywhereworks.bookmarks.repositories.FolderRepository;
import com.anywhereworks.bookmarks.services.FolderService;

@Service
public class FolderServiceImpl implements FolderService {

	private FolderRepository folderRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	public FolderServiceImpl(FolderRepository folderRepository) {
		this.folderRepository = folderRepository;
	}

	@Override
	public Folder addFolder(Folder folder) throws BusinessException {
		if (folder.getName() == null)
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Name cannot be null");
		if (folder.getName().isBlank())
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
		return folderRepository.save(folder);

	}

	@Override
	public Folder updateFolder(String folderId, Folder newFolder) throws BusinessException {

		if (newFolder.getName() == null)
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Name cannot be null");
		if (newFolder.getName().isBlank())
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Name cannot be blank");

		return folderRepository.findById(folderId).map(folder -> {
			folder.setName(newFolder.getName());
			return folderRepository.save(folder);
		}).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Folder with id " + folderId + " not found"));

	}

	@Override
	public void deleteFolder(String id) throws BusinessException {
		if (!folderRepository.existsById(id))
			throw new BusinessException(HttpStatus.NOT_FOUND, "Folder with id " + id + " doesn't exists");
		else
			folderRepository.deleteById(id);
	}

	@Override
	public Folder moveBookmarkToFolder(String id, String bookmarkId) throws BusinessException {
		Folder newFolder = folderRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Folder with id " + id + " not found"));
		Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Bookmark with id " + bookmarkId + " not found"));

		if (bookmark.getFolder() != null) {
			Folder previousFolder = folderRepository.findById(String.valueOf(bookmark.getFolderId())).get();
			previousFolder.getBookmarksSet().remove(bookmark);
			previousFolder.decrementTotalBookmarks();
			folderRepository.save(previousFolder);
		}

		newFolder.getBookmarksSet().add(bookmark);
		newFolder.incrementTotalBookmarks();
		bookmark.setFolder(newFolder);
		bookmarkRepository.save(bookmark);
		return folderRepository.save(newFolder);

	}

	@Override
	public Set<Bookmark> findAllBookmarksById(String id) throws BusinessException {
		return folderRepository.findById(id).map(folder -> folder.getBookmarksSet())
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Folder with id " + id + " not found"));

	}

}
