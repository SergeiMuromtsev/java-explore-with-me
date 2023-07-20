package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.WrongTimeException;
import ru.practicum.statdto.StatDto;
import ru.practicum.statdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Transactional
    public StatDto saveStat(StatDto statDto) {
        Stat stat = StatMapper.toStat(statDto);
        return StatMapper.toStatDto(statRepository.save(stat));
    }

    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStat(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique
    ) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new WrongTimeException("StatService getStat() wrong time");
        }

        if (uris == null) {
            if (!unique) {
                return statRepository.getStatCountForAllIp(start, end);
            }
            return statRepository.getStatCountForUniqueIp(start, end);
        }
        if (!unique) {
            return statRepository.getStatCountForAllIpByUris(start, end, uris);
        }
        return statRepository.getStatCountForUniqueIpByUris(start, end, uris);
    }
}
