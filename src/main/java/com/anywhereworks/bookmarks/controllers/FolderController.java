package com.anywhereworks.bookmarks.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anywhereworks.bookmarks.entities.Bookmark;
import com.anywhereworks.bookmarks.entities.Folder;
import com.anywhereworks.bookmarks.exception.custom.BusinessException;
import com.anywhereworks.bookmarks.services.FolderService;

@RestController
@RequestMapping("/folders")
public class FolderController {

	@Autowired
	private FolderService folderService;

	@GetMapping("/{folderId}/bookmarks")
	public ResponseEntity<Set<Bookmark>> findAllBookmarksById(@PathVariable String folderId) throws BusinessException {
		Set<Bookmark> bookmarksSet = folderService.findAllBookmarksById(folderId);
		return new ResponseEntity<>(bookmarksSet, HttpStatus.OK);
	}

	@PutMapping("/{folderId}/bookmarks/{bookmarkId}")
	public ResponseEntity<Object> moveBookmarkToFolder(@PathVariable String folderId, @PathVariable String bookmarkId)
			throws BusinessException {
		Folder updatedFolder = folderService.moveBookmarkToFolder(folderId, bookmarkId);
		return new ResponseEntity<>(updatedFolder, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Folder> addFolder(@RequestBody Folder folder) throws BusinessException {
		Folder addedFolder = folderService.addFolder(folder);
		return new ResponseEntity<>(addedFolder, HttpStatus.CREATED);

	}

	@PutMapping("/{folderId}")
	public ResponseEntity<Folder> updateFolder(@RequestBody Folder folder, @PathVariable String folderId)
			throws BusinessException {
		Folder updatedFolder = folderService.updateFolder(folderId, folder);

		return new ResponseEntity<>(updatedFolder, HttpStatus.OK);

	}

	@DeleteMapping("/{folderId}")
	public ResponseEntity<Object> deleteFolder(@PathVariable String folderId) throws BusinessException {
		folderService.deleteFolder(folderId);
		return new ResponseEntity<>("Folder with id " + folderId + " deleted successfully", HttpStatus.OK);
	}

}
