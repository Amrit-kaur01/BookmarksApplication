package com.anywhereworks.bookmarks.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.services.BookmarkService;
import com.anywhereworks.bookmarks.servicesImpl.BookmarkServiceImpl;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

	@Autowired
	private BookmarkService bookmarkService;

	@GetMapping()
	public ResponseEntity<List<Bookmark>> getAllBookmarks() throws BusinessException {
		List<Bookmark> list = bookmarkService.getAllBookmarks();
		return new ResponseEntity<>(list, HttpStatus.OK);

	}

	@GetMapping("/{bookmarkId}")
	public ResponseEntity<Bookmark> getBookmark(@PathVariable String bookmarkId) throws BusinessException {
		Bookmark bookmark = bookmarkService.getBookmark(bookmarkId);
		return new ResponseEntity<>(bookmark, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Bookmark> addBookmark(@RequestBody Bookmark bookmark) throws BusinessException {
		Bookmark addedBookmark = bookmarkService.addBookmark(bookmark);
		return new ResponseEntity<>(addedBookmark, HttpStatus.CREATED);

	}

	@PostMapping("/all")
	public ResponseEntity<List<Bookmark>> addMultipleBookmarks(@RequestBody List<Bookmark> bookmarksList)
			throws BusinessException {
		List<Bookmark> addedBookmarksList = bookmarkService.addMultipleBookmarks(bookmarksList);
		return new ResponseEntity<>(addedBookmarksList, HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<Bookmark> updateBookmark(@RequestBody Bookmark bookmark, @PathVariable String id)
			throws BusinessException {
		Bookmark updatedBookmark = bookmarkService.updateBookmark(id, bookmark);

		return new ResponseEntity<>(updatedBookmark, HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteBookmark(@PathVariable String id) throws BusinessException {
		bookmarkService.deleteBookmark(id);
		return new ResponseEntity<>("Bookmark with id " + id + " deleted successfully", HttpStatus.OK);
	}
}
