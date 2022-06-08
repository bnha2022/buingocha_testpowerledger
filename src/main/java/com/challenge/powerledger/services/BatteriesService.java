package com.challenge.powerledger.services;



import com.challenge.powerledger.repositories.BatteriesRepository;
import com.challenge.powerledger.utils.MapperUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.challenge.powerledger.models.BatteriesDTO;
import com.challenge.powerledger.models.BatteriesListDTO;
import com.challenge.powerledger.entity.Battery;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Batteries service.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class BatteriesService {

    @NonNull
    private final BatteriesRepository batteriesRepository;

    /**
     * Save list of name postcode dt os iterable.
     *
     * @param batteriesDTOs the batteries dt os
     * @return the iterable
     */
    @Transactional(rollbackFor = Exception.class)
    public Iterable<Battery> saveListOfNamePostcodeDTOs(List<BatteriesDTO> batteriesDTOs) {
        return batteriesRepository.saveAll(batteriesDTOs
                .stream()
                .map(MapperUtil::toEntity)
                .collect(Collectors.toList())
        );
    }

    /**
     * Fetch batteries list dto by postcode range batteries list dto.
     *
     * @param start the start
     * @param end   the end
     * @return the batteries list dto
     */
    @Transactional(readOnly = true)
    public BatteriesListDTO fetchBatteriesListDTOByPostcodeRange(int start, int end) {
        List<Battery> batteries = batteriesRepository.findBatteryByPostcodeBetween(start, end);
        List<String> names = batteries.stream()
                .sorted(Comparator.comparing(Battery::getBatteryName))
                .map(Battery::getBatteryName)
                .collect(Collectors.toList());
        int totalWattCapacity = batteries.stream().map(Battery::getWattCapacity).reduce(0,Integer::sum);
        int averageWattCapacity =  totalWattCapacity / batteries.size();
        return new BatteriesListDTO(names, totalWattCapacity, averageWattCapacity);
    }
}
