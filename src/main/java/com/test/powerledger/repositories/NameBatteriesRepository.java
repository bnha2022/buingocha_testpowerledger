package com.test.powerledger.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.test.powerledger.models.Battery;

import java.util.List;

@Repository
public interface NameBatteriesRepository extends CrudRepository<Battery, Integer> {

    List<Battery> findBatteryByPostcodeBetween(int min, int max);

}
