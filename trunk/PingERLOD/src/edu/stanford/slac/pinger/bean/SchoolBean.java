package edu.stanford.slac.pinger.bean;

import java.util.ArrayList;

public class SchoolBean {


	private String SchoolName, SchoolEndowment, SchoolNumberOfGradStudents, SchoolNumberOfUgradStudents, SchoolNumberOfStudents,
	SchoolFacultySize, SchoolFreebaseLink, SchoolDBPediaLink, SchoolWikipediaLink,
	geoLatitude, geoLongitude, PingERLat, PingERLong, SchoolPingerName;

	private ArrayList<String> SchoolType;
	
	public String getSchoolName() {
		return SchoolName;
	}

	public void setSchoolName(String schoolName) {
		SchoolName = schoolName;
	}

	public String getSchoolEndowment() {
		return SchoolEndowment;
	}

	public void setSchoolEndowment(String schoolEndowment) {
		SchoolEndowment = schoolEndowment;
	}

	public  ArrayList<String> getSchoolType() {
		return SchoolType;
	}

	public void setSchoolType(ArrayList<String> schoolType) {
		SchoolType = schoolType;
	}

	public String getSchoolNumberOfGradStudents() {
		return SchoolNumberOfGradStudents;
	}

	public void setSchoolNumberOfGradStudents(String schoolNumberOfGradStudents) {
		SchoolNumberOfGradStudents = schoolNumberOfGradStudents;
	}

	public String getSchoolNumberOfUgradStudents() {
		return SchoolNumberOfUgradStudents;
	}

	public void setSchoolNumberOfUgradStudents(String schoolNumberOfUgradStudents) {
		SchoolNumberOfUgradStudents = schoolNumberOfUgradStudents;
	}

	public String getSchoolNumberOfStudents() {
		return SchoolNumberOfStudents;
	}

	public void setSchoolNumberOfStudents(String schoolNumberOfStudents) {
		SchoolNumberOfStudents = schoolNumberOfStudents;
	}

	public String getSchoolFacultySize() {
		return SchoolFacultySize;
	}

	public void setSchoolFacultySize(String schoolFacultySize) {
		SchoolFacultySize = schoolFacultySize;
	}

	public String getSchoolFreebaseLink() {
		return SchoolFreebaseLink;
	}

	public void setSchoolFreebaseLink(String schoolFreebaseLink) {
		SchoolFreebaseLink = schoolFreebaseLink;
	}

	public String getSchoolDBPediaLink() {
		return SchoolDBPediaLink;
	}

	public void setSchoolDBPediaLink(String schoolDBPediaLink) {
		SchoolDBPediaLink = schoolDBPediaLink;
	}

	public String getSchoolWikipediaLink() {
		return SchoolWikipediaLink;
	}

	public void setSchoolWikipediaLink(String schoolWikipediaLink) {
		SchoolWikipediaLink = schoolWikipediaLink;
	}

	public String getGeoLatitude() {
		return geoLatitude;
	}

	public void setGeoLatitude(String geoLatitude) {
		this.geoLatitude = geoLatitude;
	}

	public String getGeoLongitude() {
		return geoLongitude;
	}

	public void setGeoLongitude(String geoLongitude) {
		this.geoLongitude = geoLongitude;
	}

	public String getPingERLat() {
		return PingERLat;
	}

	public void setPingERLat(String pingERLat) {
		PingERLat = pingERLat;
	}

	public String getPingERLong() {
		return PingERLong;
	}

	public void setPingERLong(String pingERLong) {
		PingERLong = pingERLong;
	}

	public String getSchoolPingerName() {
		return SchoolPingerName;
	}

	public void setSchoolPingerName(String schoolPingerName) {
		SchoolPingerName = schoolPingerName;
	}

	@Override
	public String toString() {
		return "SchoolBean [SchoolName=" + SchoolName + ", SchoolEndowment="
				+ SchoolEndowment + ", SchoolNumberOfGradStudents="
				+ SchoolNumberOfGradStudents + ", SchoolNumberOfUgradStudents="
				+ SchoolNumberOfUgradStudents + ", SchoolNumberOfStudents="
				+ SchoolNumberOfStudents + ", SchoolFacultySize="
				+ SchoolFacultySize + ", SchoolFreebaseLink="
				+ SchoolFreebaseLink + ", SchoolDBPediaLink="
				+ SchoolDBPediaLink + ", SchoolWikipediaLink="
				+ SchoolWikipediaLink + ", geoLatitude=" + geoLatitude
				+ ", geoLongitude=" + geoLongitude + ", PingERLat=" + PingERLat
				+ ", PingERLong=" + PingERLong + ", SchoolPingerName="
				+ SchoolPingerName + ", SchoolType=" + SchoolType + "]";
	}
	
	/*
	 * SchoolName
	 * SchoolEndowment:float
	 * SchoolType
	 * SchoolNumberOfGradStudents:integer
	 * SchoolNumberOfUgradStudents:integer
	 * SchoolNumberOfStudents:integer
	 * SchoolPingerName:string
	 * 
	 * SchoolFreebaseLink
	 * SchoolDBPediaLink
	 * SchoolWikipediaLink
	 */
	

}
