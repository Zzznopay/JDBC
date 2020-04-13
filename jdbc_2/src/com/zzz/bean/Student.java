package com.zzz.bean;

public class Student {
	private int flowID;//��ˮ��
	private int type;//��������
	private String IDCard;//���֤��
	private String examCard;//׼��֤��
	private String studentName;//ѧ������
	private String location;//���ڳ���
	private int grade;//�ɼ�
	public Student() {
		super();
	}
	public Student(int flowID, int type, String iDCard, String examCard, String studentName, String location,
			int grade) {
		super();
		this.flowID = flowID;
		this.type = type;
		IDCard = iDCard;
		this.examCard = examCard;
		this.studentName = studentName;
		this.location = location;
		this.grade = grade;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIDCard() {
		return IDCard;
	}
	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}
	public String getExamCard() {
		return examCard;
	}
	public void setExamCard(String examCard) {
		this.examCard = examCard;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getFlowID() {
		return flowID;
	}
	@Override
	public String toString() {
		System.out.println("=========��ѯ���===========");
		return info();
	}
	private String info() {
		return "��ˮ�ţ�" + flowID + "\n�ļ�/������" + type + "\n���֤�ţ�" + IDCard + "\n׼��֤�ţ�" + examCard + 
				"\nѧ��������" + studentName + "\n����" + location + "\n�ɼ���" + grade;
	}
	
}
