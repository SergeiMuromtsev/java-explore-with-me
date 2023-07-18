package ru.practicum.compilations.publ.service;

import ru.practicum.compilations.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    List<CompilationDto> getCompilations(String pinned, int from, int size);

    CompilationDto getCompilationsById(int compId);
}
