package com.anywhereworks.bookmarks.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.anywhereworks.bookmarks.entities.Folder;

import jakarta.transaction.Transactional;

public interface FolderRepository extends CrudRepository<Folder, Long> {

	@Modifying @Transactional
	@Query("update Bookmark b set b.folder = (select f from Folder f where f.folderId=folderId) where b.bookmarkId = bookmarkId")
	public void moveBookmarkToFolder(@Param("folderId") Long folderId,@Param("bookmarkId") Long bookmarkId);
}
