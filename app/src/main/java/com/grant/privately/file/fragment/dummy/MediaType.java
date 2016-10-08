package com.grant.privately.file.fragment.dummy;

/***
 * 选择的媒体的类型
 *
 */
public enum MediaType {
	/**图片*/
	IMAGE(0),
	/**视频*/
	VIDEO(1),
	/**音频*/
	VOICE(2);


	private int index = 0;
	MediaType(int i) {
		this.index = i;
	}

	public int getIndex(){
		return index;
	}
}