package com.challenge.powerledger.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.challenge.powerledger.entity.Battery;

import java.util.List;

@Repository
public interface BatteriesRepository extends CrudRepository<Battery, Integer> {

    /**
     * Find battery by postcode between list.
     *
     * @param min the min
     * @param max the max
     * @return the list
     */
    List<Battery> findBatteryByPostcodeBetween(int min, int max);

}
