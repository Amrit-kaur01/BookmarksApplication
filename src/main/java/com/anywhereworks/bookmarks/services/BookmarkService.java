package com.anywhereworks.bookmarks.services;

import java.util.List;
import java.util.Optional;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;

public interface BookmarkService {
	List<Bookmark> getAllBookmarks() throws BusinessException;

	Bookmark getBookmark(Long bookmarkId) throws BusinessException;

	Bookmark addBookmark(Bookmark bookmark) throws BusinessException;

	Bookmark updateBookmark(Long bookmarkId, Bookmark bookmark) throws BusinessException;

	void deleteBookmark(Long bookmarkId) throws BusinessException;

	List<Bookmark> addMultipleBookmarks(List<Bookmark> bookmarksList) throws BusinessException;
}
