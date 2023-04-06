package com.anywhereworks.bookmarks.entities;

import java.util.Objects;
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
	private long folderId;

	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
	private Set<Bookmark> bookmarksSet;

	private int totalBookmarks;

	public Folder() {
	}

	public Folder(long folderId, Set<Bookmark> bookmarksSet, String name) {
		super();
		this.folderId = folderId;
		this.bookmarksSet = bookmarksSet;
		this.name = name;

	}

	public int getTotalBookmarks() {
		return totalBookmarks;
	}

	public void setTotalBookmarks(int totalBookmarks) {
		this.totalBookmarks = totalBookmarks;
	}

	public long getFolderId() {
		return folderId;
	}

	public void setFolderId(long id) {
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Folder other = (Folder) obj;
		return Objects.equals(bookmarksSet, other.bookmarksSet) && folderId == other.folderId
				&& Objects.equals(name, other.name) && totalBookmarks == other.totalBookmarks;
	}

}