package com.anywhereworks.bookmarks.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "folders")
public class Folder {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int folderId;

	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
	private Set<Bookmark> bookmarksSet;

	private int totalBookmarks;

	public Folder() {
	}

	public Folder(int id, Set<Bookmark> bookmarksSet, String name) {
		super();
		this.folderId = id;
		this.bookmarksSet = bookmarksSet;
		this.name = name;

	}

	public int getTotalBookmarks() {
		return totalBookmarks;
	}

	public void setTotalBookmarks(int totalBookmarks) {
		this.totalBookmarks = totalBookmarks;
	}

	public int getId() {
		return folderId;
	}

	public void setId(int id) {
		this.folderId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Bookmark> getBookmarksSet() {
		return bookmarksSet;
	}

	public void setBookmarksSet(Set<Bookmark> bookmarksSet) {
		this.bookmarksSet = bookmarksSet;
	}

	public void incrementTotalBookmarks() {
		totalBookmarks++;
	}

	public void decrementTotalBookmarks() {
		totalBookmarks--;
	}

}
