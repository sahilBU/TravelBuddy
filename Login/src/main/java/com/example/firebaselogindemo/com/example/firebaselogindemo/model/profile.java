package com.example.firebaselogindemo.com.example.firebaselogindemo.model;

public class profile {

    private String Name;
    private String Gender;
    private String Age;
    private String Phone;
    private String Email;
    private String Location;

    public profile(){
    }

    public profile(String Name, String Gender,String Age,String Phone, String Email,String Location ){
        this.Name = Name;
        this.Gender = Gender;
        this.Age = Age;
        this.Phone = Phone;
        this.Email = Email;
        this.Location = Location;
    }

    public String getName() {return Name;}
    public void setName(String Name) { this.Name = Name; }

    public String getGender(){ return Gender;}
    public void setGender(String Gender){ this.Gender = Gender; }

    public String getAge(){ return Age; }
    public void setAge(String Age){ this.Age = Age; }

    public String getPhone(){ return Phone; }
    public void setPhone(String Phone){ this.Phone = Phone; }

    public String getEmail(){ return Email; }
    public void setEmail(String Email){ this.Email = Email; }

    public String getLocation(){ return Location; }
    public void setLocation(String Location){ this.Location = Location; }

}
