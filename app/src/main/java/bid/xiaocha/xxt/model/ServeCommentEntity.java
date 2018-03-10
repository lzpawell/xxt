package bid.xiaocha.xxt.model;

import java.util.Date;

public class ServeCommentEntity {
	private int serveCommentId;
	private String serveId;
    private double mark;
	private String commentContent;
	private long commentDate;
	private String commentorId;//评论人
	public String getCommentorId() {
		return commentorId;
	}
	public void setCommentorId(String commentorId) {
		this.commentorId = commentorId;
	}

	public long getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(long commentDate) {
		this.commentDate = commentDate;
	}

	public int getServeCommentId() {
		return serveCommentId;
	}
	public void setServeCommentId(int serveCommentId) {
		this.serveCommentId = serveCommentId;
	}
	public String getServeId() {
		return serveId;
	}
	public void setServeId(String serveId) {
		this.serveId = serveId;
	}
	public double getMark() {
		return mark;
	}
	public void setMark(double mark) {
		this.mark = mark;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	
	
}