package com.anywhereworks.bookmarks.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "folders")
public class Folder implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long folderId;

	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
	private Set<Bookmark> bookmarksSet;

	private int totalBookmarks;

	@Version
	private long version;

	public Folder(long folderId, Set<Bookmark> bookmarksSet, String name) {
		super();
		this.folderId = folderId;
		this.bookmarksSet = bookmarksSet;
		this.name = name;

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