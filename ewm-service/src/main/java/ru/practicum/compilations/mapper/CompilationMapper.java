package ru.practicum.compilations.mapper;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.model.Event;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.events.mapper.EventsMapper.toListEventShortDto;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> eventList) {
        return Compilation.builder()
                .eventsWithCompilations(eventList)
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilations) {
        return CompilationDto.builder()
                .id(compilations.getId())
                .events(toListEventShortDto(compilations.getEventsWithCompilations()))
                .pinned(compilations.isPinned())
                .title(compilations.getTitle())
                .build();
    }

    public static List<CompilationDto> toListCompilationsDto(List<Compilation> listCompilations) {
        List<CompilationDto> listCompilationDto = new ArrayList<>();
        for (Compilation compilation : listCompilations) {
            listCompilationDto.add(toCompilationDto(compilation));
        }
        return listCompilationDto;
    }
}
