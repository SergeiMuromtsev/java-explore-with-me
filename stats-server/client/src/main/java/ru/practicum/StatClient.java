package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {
    public StatClient(@Value("${web-server.url}") String serverUrl) {
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
        if (uris != null) {
            parameters.put("uris", uris);
        }

        ResponseEntity<Object> response = get("/stats", parameters);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewStatsDto> viewStatsDtos = objectMapper.convertValue(
                response.getBody(),
                new TypeReference<List<ViewStatsDto>>() {}
        );

        return viewStatsDtos;
    }

}