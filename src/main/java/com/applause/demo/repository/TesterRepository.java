package com.applause.demo.repository;

import com.applause.demo.entity.Tester;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TesterRepository extends CrudRepository<Tester, Long> {
    @Query("SELECT DISTINCT country FROM Tester")
    List<String> findAllCountries();

    List<Tester> findDistinctByCountryIn(List<String> countries);
    List<Tester> findDistinctByDevices_IdIn(List<Long> deviceIds);
    List<Tester> findDistinctByCountryInAndDevices_IdIn(List<String> countries, List<Long> deviceIds);
}
