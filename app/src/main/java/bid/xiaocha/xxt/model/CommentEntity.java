package bid.xiaocha.xxt.model;

import java.util.Date;

public class CommentEntity {
	private int commentId;
	private short type;//对于被评论人是充当怎样身份的type
    private String commentorId;//评论人
    private String commentContent;
	private int marks;
    private String commentedId;//被评论人
    private long commentDate;
	public long getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(long commentDate) {
		this.commentDate = commentDate;
	}
    
    public static final short PROVIDER = 1;
	public static final short DEMANDER = 2;
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public String getCommentorId() {
		return commentorId;
	}
	public void setCommentorId(String commentorId) {
		this.commentorId = commentorId;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public String getCommentedId() {
		return commentedId;
	}
	public void setCommentedId(String commentedId) {
		this.commentedId = commentedId;
	}
    
}
