package com.anywhereworks.bookmarks.services;

import java.util.Set;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;

import java.util.Optional;

public interface FolderService {

	Folder addFolder(Folder folder) throws BusinessException;

	Folder updateFolder(Long folderId, Folder folder) throws BusinessException;

	void deleteFolder(Long folderId) throws BusinessException;

	Folder moveBookmarkToFolder(Long folderId, Long bookmarkId) throws BusinessException;

	Folder getFolder(Long folderId) throws BusinessException;
}
