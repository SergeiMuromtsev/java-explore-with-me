package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Transactional(readOnly = false)
    public StatDto saveStat(StatDto statDto) {
        Stat stat = StatMapper.toStat(statDto);
        return StatMapper.toStatDto(statRepository.save(stat));
    }

    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (uris == null) { //все uri
            if (!unique) {
                return statRepository.getStatCountForAllIp(start, end);
            }
            return statRepository.getStatCountForUniqueIp(start, end);
        }

        //если не уникальные
        if (!unique) {
            return statRepository.getStatCountForAllIpByUris(start, end, uris);
        }
        return statRepository.getStatCountForUniqueIpByUris(start, end, uris);
    }

}

