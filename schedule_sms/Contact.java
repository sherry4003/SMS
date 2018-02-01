package com.example.drago.schedule_sms;

/**
 * Created by SherryDang on 11/22/2017.
 */

public class Contact {
    private int id;
    private String name;
    private String phoneNum;
    private String time;
    private String date;
    private String message;

    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    public Contact (int id, String name, String message, String date, String time){
        this.id = id;
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
    }
    public Contact ( String name, String message, String date, String time){
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;

    }

    public void setDetails(int newMinute, int newHour, int newDay, int newMonth, int newYear, String number){
        this.hour = newHour;
        this.minute = newMinute;
        this.year = newYear;
        this.month = newMonth;
        this.day = newDay;
        this.phoneNum = number;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
