package ru.practicum.compilations.admin.service;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.CompilationUpdateRequestDto;

public interface AdminCompilationsService {
    CompilationDto createCompilations(NewCompilationDto newCompilationDto);

    void deleteCompilations(int compId);

    CompilationDto changeCompilations(int compId, CompilationUpdateRequestDto updateCompilationRequest);
}