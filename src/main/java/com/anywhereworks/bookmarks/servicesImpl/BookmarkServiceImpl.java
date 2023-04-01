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
	public Bookmark getBookmark(String bookmarkId) throws BusinessException {
		return bookmarkRepository.findById(bookmarkId).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Bookmark with id " + bookmarkId + " not found"));

	}

	@Override
	public Bookmark addBookmark(Bookmark bookmark) throws BusinessException {
		if (bookmark.getTitle() == null)
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Title cannot be null");
		if (bookmark.getTitle().isEmpty())
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Title cannot be empty");
		else
			return bookmarkRepository.save(bookmark);

	}

	@Override
	public Bookmark updateBookmark(String bookmarkId, Bookmark newBookmark) throws BusinessException {

		if (newBookmark.getTitle() == null)
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Title cannot be null");
		if (newBookmark.getTitle().isEmpty())
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Title cannot be empty");
		else {
			return bookmarkRepository.findById(bookmarkId).map(bookmark -> {
				bookmark.setTitle(newBookmark.getTitle());
				bookmark.setUrl(newBookmark.getUrl());
				return bookmarkRepository.save(bookmark);
			}).orElseThrow(
					() -> new BusinessException(HttpStatus.NOT_FOUND, "Bookmark with id " + bookmarkId + " not found"));
		}

	}

	@Override
	public void deleteBookmark(String id) throws BusinessException {

		Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(
				() -> new BusinessException(HttpStatus.NOT_FOUND, "Bookmark with id " + id + " doesn't exists"));

		if (bookmark.getFolder() == null)
			bookmarkRepository.deleteById(id);
		else {
			Folder folder = folderRepository.findById(String.valueOf(bookmark.getFolderId())).get();
			folder.getBookmarksSet().remove(bookmark);
			folder.decrementTotalBookmarks();
			folderRepository.save(folder);
			bookmarkRepository.deleteById(id);

		}
	}

	@Override
	public List<Bookmark> addMultipleBookmarks(List<Bookmark> bookmarksList) throws BusinessException {
		List<Bookmark> bookmarks = new ArrayList<>();
		bookmarksList.forEach(bookmark -> {
			try {
				bookmarks.add(addBookmark(bookmark));
			} catch (BusinessException e) {
			}
		});
		return bookmarks;
	}

}
