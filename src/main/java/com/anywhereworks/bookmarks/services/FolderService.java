package com.anywhereworks.bookmarks.services;

import java.util.Set;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;

import java.util.Optional;

public interface FolderService {

	Folder addFolder(Folder folder) throws BusinessException;

	Folder updateFolder(String folderId, Folder folder) throws BusinessException;

	void deleteFolder(String folderId) throws BusinessException;

	Folder moveBookmarkToFolder(String folderId, String bookmarkId) throws BusinessException;

	Set<Bookmark> findAllBookmarksById(String folderId) throws BusinessException;
}
