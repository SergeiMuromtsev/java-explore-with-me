package ru.practicum.locations.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.locations.model.Location;
import ru.practicum.locations.repository.LocationRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;

    @Override
    public Location save(Location location) {
        return repository.save(location);
    }
}
