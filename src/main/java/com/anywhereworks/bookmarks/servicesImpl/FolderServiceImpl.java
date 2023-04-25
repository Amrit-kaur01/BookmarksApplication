package com.anywhereworks.bookmarks.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.helpers.HelperClass;
import com.anywhereworks.bookmarks.repositories.BookmarkRepository;
import com.anywhereworks.bookmarks.repositories.FolderRepository;
import com.anywhereworks.bookmarks.services.FolderService;

import jakarta.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class FolderServiceImpl implements FolderService {

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Override
	@CachePut(value = "folders", key = "#result.folderId")
	public Folder addFolder(Folder folder) throws BusinessException {
		if (!HelperClass.validateAttribute(folder.getName()))
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Name is invalid");

		if (folder.getBookmarksSet() != null) {
			folder.setTotalBookmarks(folder.getBookmarksSet().size());
			folder.getBookmarksSet().forEach(bookmark -> bookmark.setFolder(folder));
		}

		return folderRepository.save(folder);

	}

	@Override
	@CachePut(value = "folders", key = "#folderId")
	public Folder updateFolder(long folderId, Folder newFolder) throws BusinessException {

		if (!HelperClass.validateAttribute(newFolder.getName()))
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Name is invalid");

		return folderRepository.findById(folderId).map(folder -> {
			folder.setName(newFolder.getName());
			return folderRepository.save(folder);
		}).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Folder with id " + folderId + " not found"));

	}

	@Override
	@CacheEvict(value = "folders", allEntries = false, key = "#folderId")
	public void deleteFolder(long folderId) throws BusinessException {
		folderRepository.deleteById(folderId);
	}

	@Override
	@CachePut(value = "folders", key = "#folderId")
	public Folder moveBookmarkToFolder(long folderId, long bookmarkId) throws BusinessException {
		Folder newFolder = folderRepository.findById(folderId).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Folder with id " + folderId + " not found"));
		Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Bookmark with id " + bookmarkId + " not found"));

		if (bookmark.getFolder() != null) {
			Folder previousFolder = folderRepository.findById(bookmark.getFolderId()).get();
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
	@Cacheable(value = "folders", key = "#folderId")
	public Folder getFolder(long folderId) throws BusinessException {
		Folder folder = folderRepository.findById(folderId).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Folder with id " + folderId + " not found"));

		return folder;
	}

}
