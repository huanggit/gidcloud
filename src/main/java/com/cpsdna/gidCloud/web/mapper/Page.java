package com.cpsdna.gidCloud.web.mapper;

public class Page {
	private int no = 0;
	private int size = 10;
	private int records;

	public Page(int no) {
		this.no = no;
	}
	public Page(int no, int size) {
		this.no = no;
		this.size = size;
	}

	public int getPages() {
		return (int) Math.ceil((double)records / size);
	}

	public int getStart() {
		return no * size;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}

}
