package com.anywhereworks.bookmarks.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anywhereworks.bookmarks.entities.Folder;

public interface FolderRepository extends CrudRepository<Folder, String> {

}
