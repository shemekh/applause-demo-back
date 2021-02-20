package com.applause.demo.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "AD_TESTER")
public class Tester implements Serializable {

    @JsonProperty("testerId")
    @Id
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String country;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Device> devices;

    @OneToMany(mappedBy = "tester", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Bug> bugs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public Set<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(Set<Bug> bugs) {
        this.bugs = bugs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, country, lastLogin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Tester other = (Tester) obj;
        return Objects.equals(id, other.getId()) && Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName) && Objects.equals(country, other.country)
                && Objects.equals(lastLogin, other.lastLogin);
    }
}
