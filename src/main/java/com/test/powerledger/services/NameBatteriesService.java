package com.test.powerledger.services;



import com.test.powerledger.repositories.NameBatteriesRepository;
import com.test.powerledger.utils.MapperUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.test.powerledger.models.BatteriesDTO;
import com.test.powerledger.models.BatteriesListDTO;
import com.test.powerledger.models.Battery;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NameBatteriesService {

    @NonNull
    private final NameBatteriesRepository nameBatteriesRepository;

    @Transactional(rollbackFor = Exception.class)
    public Iterable<Battery> saveListOfNamePostcodeDTOs(List<BatteriesDTO> batteriesDTOs) {
        return nameBatteriesRepository.saveAll(batteriesDTOs
                .stream()
                .map(MapperUtil::toEntity)
                .collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    public BatteriesListDTO fetchBatteriesListDTOByPostcodeRange(int start, int end) {
        List<Battery> batteries = nameBatteriesRepository.findBatteryByPostcodeBetween(start, end);
        List<String> names = batteries.stream()
                .sorted(Comparator.comparing(Battery::getBatteryName))
                .map(Battery::getBatteryName)
                .collect(Collectors.toList());
        int totalWattCapacity = batteries.stream().map(Battery::getWattCapacity).reduce(0,Integer::sum);
        double averageWattCapacity =  totalWattCapacity / batteries.size();
        return new BatteriesListDTO(names, totalWattCapacity, averageWattCapacity);
    }
}
