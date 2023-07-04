package ru.practicum.statclient;

import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.statdto.StatDto;
import ru.practicum.statdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatClient extends BaseClient {
    private ObjectMapper objectMapper = new ObjectMapper();

    public StatClient(@Value("${web-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public void saveStat(StatDto statDto) {
        try {
            post("/hit", statDto);
        } catch (Exception e) {
            log.warn("Error in accessing the server at POST /hit: {}", e.getMessage());
        }
    }

    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(formatter));
        parameters.put("end", end.format(formatter));
        parameters.put("unique", unique);
        if (uris != null) {
            parameters.put("uris", uris);
        }
        ResponseEntity<Object> response;
        try {
            response = get("/stats", parameters);
        } catch (Exception e) {
            log.warn("Error in accessing the server at GET /stats: {}", e.getMessage());
            return null;
        }

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