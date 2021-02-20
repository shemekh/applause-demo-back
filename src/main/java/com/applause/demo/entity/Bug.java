package com.applause.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "AD_BUG")
public class Bug implements Serializable {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "testerId", nullable = false)
    private Tester tester;

    @ManyToOne
    @JoinColumn(name = "deviceId", nullable = false)
    private Device device;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, device, tester);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Bug other = (Bug) obj;
        return Objects.equals(id, other.getId())
                && Objects.equals(device, other.device)
                && Objects.equals(tester, other.tester);
    }
}
