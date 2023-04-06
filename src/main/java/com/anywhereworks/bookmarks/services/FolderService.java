package com.anywhereworks.bookmarks.services;

import java.util.Set;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;

import java.util.Optional;

public interface FolderService {

	Folder addFolder(Folder folder) throws BusinessException;

	Folder updateFolder(long folderId, Folder folder) throws BusinessException;

	void deleteFolder(long folderId) throws BusinessException;

	Folder moveBookmarkToFolder(long folderId, long bookmarkId) throws BusinessException;

	Folder getFolder(long folderId) throws BusinessException;
}
