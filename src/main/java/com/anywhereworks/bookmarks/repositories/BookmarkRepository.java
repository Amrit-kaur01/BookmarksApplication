package com.anywhereworks.bookmarks.repositories;

import org.springframework.data.repository.CrudRepository;

import com.anywhereworks.bookmarks.entities.Bookmark;

public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {

}
