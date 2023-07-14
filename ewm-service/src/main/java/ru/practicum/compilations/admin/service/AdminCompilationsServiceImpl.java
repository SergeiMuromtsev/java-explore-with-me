package ru.practicum.compilations.admin.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationUpdateRequestDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.events.priv.service.PrivateEventsService;
import ru.practicum.events.model.Event;
import ru.practicum.exceptions.NotFoundException;


import java.util.ArrayList;
import java.util.List;

import static ru.practicum.compilations.mapper.CompilationMapper.toCompilation;
import static ru.practicum.compilations.mapper.CompilationMapper.toCompilationDto;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    @Autowired
    private CompilationsRepository compilationsRepository;
    @Autowired
    private PrivateEventsService privateEventsService;

    @Override
    public CompilationDto createCompilations(NewCompilationDto newCompilationDto) {
        List<Event> eventList = new ArrayList<>();

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            for (Integer eventId : newCompilationDto.getEvents()) {
                eventList.add(privateEventsService.getEventById(eventId));
            }
        }
        return toCompilationDto(compilationsRepository.save(toCompilation(newCompilationDto, eventList)));
    }

    @Override
    public void deleteCompilations(int compId) {
        compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("ExceptionMessages.NOT_FOUND_COMPILATIONS_EXCEPTION.label"));
        log.debug("LogMessages.ADMIN_DELETE_COMPILATIONS_ID.label, compId");
        compilationsRepository.deleteById(compId);
    }

    @Override
    public CompilationDto changeCompilations(int compId, CompilationUpdateRequestDto compilationUpdateRequestDto) {
        Compilation oldCompilations = compilationsRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));

        if (compilationUpdateRequestDto.getEvents() != null && !compilationUpdateRequestDto.getEvents().isEmpty()) {
            List<Event> eventList = new ArrayList<>();

            for (Integer eventId : compilationUpdateRequestDto.getEvents()) {
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