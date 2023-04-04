package com.anywhereworks.bookmarks.servicesImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.helpers.HelperClass;
import com.anywhereworks.bookmarks.repositories.BookmarkRepository;
import com.anywhereworks.bookmarks.repositories.FolderRepository;
import com.anywhereworks.bookmarks.services.BookmarkService;

@Service
public class BookmarkServiceImpl implements BookmarkService {

	private BookmarkRepository bookmarkRepository;

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
		this.bookmarkRepository = bookmarkRepository;
	}

	@Override
	public List<Bookmark> getAllBookmarks() {
		List<Bookmark> bookmarks = new ArrayList<>();
		bookmarkRepository.findAll().forEach(bookmarks::add);

		return bookmarks;
	}

	@Override
	public Bookmark getBookmark(Long bookmarkId) throws BusinessException {
		return bookmarkRepository.findById(bookmarkId).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Bookmark with id " + bookmarkId + " not found"));

	}

	@Override
	public Bookmark addBookmark(Bookmark bookmark) throws BusinessException {
		if (!HelperClass.validateAttribute(bookmark.getTitle()))
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Title is invalid");

		return bookmarkRepository.save(bookmark);

	}

	@Override
	public Bookmark updateBookmark(Long bookmarkId, Bookmark newBookmark) throws BusinessException {

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
	public void deleteBookmark(Long bookmarkId) throws BusinessException {

		bookmarkRepository.deleteById(bookmarkId);

	}

	@Override
	public List<Bookmark> addMultipleBookmarks(List<Bookmark> bookmarksList) throws BusinessException {
		List<Bookmark> bookmarks = new ArrayList<>();
		bookmarksList.forEach(bookmark -> {
			try {
				bookmarks.add(addBookmark(bookmark));
			} catch (BusinessException e) {
				System.out.println(e);
			}
		});
		return bookmarks;
	}

}
