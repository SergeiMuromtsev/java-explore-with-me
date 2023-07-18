package ru.practicum.compilations.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationUpdateRequestDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.events.priv.PrivateEventsService;
import ru.practicum.events.model.Event;
import ru.practicum.exceptions.NotFoundException;


import java.util.ArrayList;
import java.util.List;

import static ru.practicum.compilations.mapper.CompilationMapper.toCompilation;
import static ru.practicum.compilations.mapper.CompilationMapper.toCompilationDto;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationsServiceImpl implements AdminCompilationsService {

    private final CompilationsRepository compilationsRepository;
    private final PrivateEventsService privateEventsService;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> eventList = new ArrayList<>();

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            for (Long eventId : newCompilationDto.getEvents()) {
                eventList.add(privateEventsService.getEventById(eventId));
            }
        }
        return toCompilationDto(compilationsRepository.save(toCompilation(newCompilationDto, eventList)));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compilationsRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("compilation " + compilationId + " not found"));
        compilationsRepository.deleteById(compilationId);
    }

    @Override
    public CompilationDto changeCompilation(Long compilationId, CompilationUpdateRequestDto compilationUpdateRequestDto) {
        Compilation oldCompilations = compilationsRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("compilation " + compilationId + " not found"));

        if (compilationUpdateRequestDto.getEvents() != null && !compilationUpdateRequestDto.getEvents().isEmpty()) {
            List<Event> eventList = new ArrayList<>();

            for (Long eventId : compilationUpdateRequestDto.getEvents()) {
                eventList.add(privateEventsService.getEventById(eventId));
            }

            oldCompilations.setEventsWithCompilations(eventList);
        }
        if (compilationUpdateRequestDto.getPinned() != null) {
            oldCompilations.setPinned(compilationUpdateRequestDto.getPinned());
        }
        if (compilationUpdateRequestDto.getTitle() != null && !compilationUpdateRequestDto.getTitle().isEmpty()) {
            oldCompilations.setTitle(compilationUpdateRequestDto.getTitle());
        }
        return toCompilationDto((compilationsRepository.save(oldCompilations)));
    }
}