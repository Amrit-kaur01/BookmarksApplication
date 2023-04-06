package com.anywhereworks.bookmarks.entities;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
public class Bookmark {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long bookmarkId;

	@Column(nullable = false)
	private String title;
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FolderId")
	@JsonIgnore
	private Folder folder;

	public Bookmark() {

	}

	public Bookmark(long bookmarkId, String title, String url) {
		super();
		this.bookmarkId = bookmarkId;
		this.title = title;
		this.url = url;

	}

	public long getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(long bookmarkId) {
		this.bookmarkId = bookmarkId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public long getFolderId() {
		if (folder != null)
			return folder.getFolderId();
		return 0;
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
		Bookmark other = (Bookmark) obj;
		return bookmarkId == other.bookmarkId && Objects.equals(folder, other.folder)
				&& Objects.equals(title, other.title) && Objects.equals(url, other.url);
	}

}