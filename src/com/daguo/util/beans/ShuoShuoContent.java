package com.daguo.util.beans;

/**
 * 说说的实体类，记录主页说说要显示的内容 详细的不算
 * 
 * @author Bugs_Rabbit 時間： 2015-8-20 下午1:50:54
 */
public class ShuoShuoContent {
	private String content;// 内容
	private String creatTime;// 创建时间
	private String id;// 表的id 创建时返回的
	private String p_id;// 个人信息表id 关联个人
	private String type; // 帖子分类
	private String img_path;// 照片的路径
	private String good_count; // 点赞数
	private String feedback_count;// 评论数
	private String type_name;// 分类名称
	private String p_name;// 发帖人，省去查询的请求
	private String p_photo;// 头像信息

	public ShuoShuoContent() {
		super();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getP_id() {
		return p_id;
	}

	public void setP_id(String p_id) {
		this.p_id = p_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getGood_count() {
		return good_count;
	}

	public void setGood_count(String good_count) {
		this.good_count = good_count;
	}

	public String getFeedback_count() {
		return feedback_count;
	}

	public void setFeedback_count(String feedback_count) {
		this.feedback_count = feedback_count;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public String getP_photo() {
		return p_photo;
	}

	public void setP_photo(String p_photo) {
		this.p_photo = p_photo;
	}

}
