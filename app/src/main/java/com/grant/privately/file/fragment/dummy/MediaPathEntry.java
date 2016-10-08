package com.grant.privately.file.fragment.dummy;
public class MediaPathEntry {

	private MediaType mediaType;
	/**
	 * 缩略图路径
	 */
	private String thumbPath;
	/**
	 * 原图路径
	 */
	private String imgPath;
	/**
	 * 是否被选中
	 */
	private boolean isSelected = false;
	
	public MediaPathEntry(){}
	
	public MediaPathEntry(String thumbPath, String imgPath, boolean isSelected) {
		this.thumbPath = thumbPath;
		this.imgPath = imgPath;
		this.isSelected = isSelected;
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
}