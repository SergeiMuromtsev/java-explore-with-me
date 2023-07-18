package ru.practicum.compilations.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.admin.service.AdminCompilationsService;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationUpdateRequestDto;
import ru.practicum.compilations.dto.NewCompilationDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationsController {
    private final AdminCompilationsService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/compilations")
    public CompilationDto createCompilations(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.debug("");
        return service.createCompilations(newCompilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/compilations/{compId}")
    public void deleteCompilations(@PathVariable(name = "compId") int compId) {
        log.debug("LogMessages.TRY_ADMIN_DELETE_COMPILATIONS_ID.label, compId");
        service.deleteCompilations(compId);
    }

    @PatchMapping(path = "/compilations/{compId}")
    public CompilationDto changeCompilations(@PathVariable(name = "compId") int compId,
                                             @RequestBody @Valid CompilationUpdateRequestDto compilationUpdateRequestDto) {
        log.debug("LogMessages.TRY_ADMIN_PATCH_COMPILATIONS_ID.label, compId");
        return service.changeCompilations(compId, compilationUpdateRequestDto);
    }
}