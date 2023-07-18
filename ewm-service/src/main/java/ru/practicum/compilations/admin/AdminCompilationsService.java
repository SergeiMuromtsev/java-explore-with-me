package ru.practicum.compilations.admin;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.CompilationUpdateRequestDto;

public interface AdminCompilationsService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto changeCompilation(Long compId, CompilationUpdateRequestDto updateCompilationRequest);
}