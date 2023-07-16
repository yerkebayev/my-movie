package mymovie.model;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    public String id;

    public String userId;
    public String gender;
    public int age;
    public int occupation;

    public User () {}

    public User(
            String userId,
            String gender,
            int age,
            int occupation
    ) {
        this.userId = userId;
        this.gender = gender;
        this.age = age;
        this.occupation = occupation;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public void setGender (String gender) {
        this.gender = gender;
    }

    public void setAge (String age) {
        this.age = Integer.parseInt(age);
    }

    public void setOccupation (String occupation) {
        this.occupation = Integer.parseInt(occupation);
    }

    public void setZipCode (String zipCode) {
        return;
    }
}
