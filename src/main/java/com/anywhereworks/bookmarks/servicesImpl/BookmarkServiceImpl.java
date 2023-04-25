package com.anywhereworks.bookmarks.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.helpers.HelperClass;
import com.anywhereworks.bookmarks.repositories.BookmarkRepository;
import com.anywhereworks.bookmarks.repositories.FolderRepository;
import com.anywhereworks.bookmarks.services.BookmarkService;

import jakarta.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class BookmarkServiceImpl implements BookmarkService {
	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	private FolderRepository folderRepository;

	@Override
	public List<Bookmark> getAllBookmarks() {
		List<Bookmark> bookmarks = new ArrayList<>();
		bookmarkRepository.findAll().forEach(bookmarks::add);

		return bookmarks;
	}

	@Override
	@Cacheable(value = "bookmarks", key = "#bookmarkId")
	public Bookmark getBookmark(long bookmarkId) throws BusinessException {
		return bookmarkRepository.findById(bookmarkId).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Bookmark with id " + bookmarkId + " not found"));

	}

	@Override
	@CachePut(value = "bookmarks", key = "#result.bookmarkId")
	public Bookmark addBookmark(Bookmark bookmark) throws BusinessException {
		if (!HelperClass.validateAttribute(bookmark.getTitle()))
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Title is invalid");

		return bookmarkRepository.save(bookmark);

	}

	@Override
	@CachePut(value = "bookmarks", key = "#bookmarkId")
	public Bookmark updateBookmark(long bookmarkId, Bookmark newBookmark) throws BusinessException {

		if (!HelperClass.validateAttribute(newBookmark.getTitle()))
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Title is invalid");

		return bookmarkRepository.findById(bookmarkId).map(bookmark -> {
			bookmark.setTitle(newBookmark.getTitle());
			bookmark.setUrl(newBookmark.getUrl());
			return bookmarkRepository.save(bookmark);
		}).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Bookmark with id " + bookmarkId + " not found"));

	}

	@Override
	@Caching(evict = {@CacheEvict(value = "bookmarks", allEntries = false, key = "#bookmarkId"), @CacheEvict(value="folders", allEntries = true)})
	public void deleteBookmark(long bookmarkId) throws BusinessException {

		Optional<Bookmark> bookmark = bookmarkRepository.findById(bookmarkId);
		if (bookmark.isPresent() && bookmark.get().getFolder()!=null) {
			bookmark.get().getFolder().decrementTotalBookmarks();
			folderRepository.save(bookmark.get().getFolder());
		}

		bookmarkRepository.deleteById(bookmarkId);

	}

	@Override
	public List<Bookmark> addMultipleBookmarks(List<Bookmark> bookmarksList) throws BusinessException {
		List<Bookmark> bookmarks = new ArrayList<>();
		var wrapper = new Object() {
			String errorMessage = "";
		};
		bookmarksList.forEach(bookmark -> {
			if (!HelperClass.validateAttribute(bookmark.getTitle())) {
				wrapper.errorMessage = "Title is invalid for one or more bookmarks";
				return;
			}

			bookmarks.add(bookmarkRepository.save(bookmark));
		});
		if (wrapper.errorMessage != null)
			throw new BusinessException(HttpStatus.BAD_REQUEST, wrapper.errorMessage);
		return bookmarks;
	}

}
