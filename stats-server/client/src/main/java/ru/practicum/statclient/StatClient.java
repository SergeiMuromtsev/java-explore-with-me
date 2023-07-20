package ru.practicum.statclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.statdto.StatDto;
import ru.practicum.statdto.ViewStatsDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatClient extends BaseClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public StatClient(@Value("${stats-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public void saveStat(StatDto statDto) {
        post("/hit", statDto);
    }

    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(formatter));
        parameters.put("end", end.format(formatter));
        parameters.put("unique", unique);
        if (uris != null && uris.size() != 0) {
            parameters.put("uris", uris);
        }

        ResponseEntity<Object> response;
        response = get("/stats", parameters);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<ViewStatsDto> viewStatsDtos = objectMapper.convertValue(
                    response.getBody(),
                    new TypeReference<List<ViewStatsDto>>() {
                    }
            );
            return viewStatsDtos;
        } else {
            log.warn("Error in server response: {}", response.getStatusCode());
            return null;
        }
    }

}