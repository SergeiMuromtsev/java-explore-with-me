package ru.practicum.compilations.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationUpdateRequestDto;
import ru.practicum.compilations.dto.NewCompilationDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationsController {
    private final AdminCompilationsService adminCompilationsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/compilations")
    public CompilationDto createCompilations(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.debug("POST: /admin/compilations");
        return adminCompilationsService.createCompilation(newCompilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/compilations/{compilationId}")
    public void deleteCompilations(@PathVariable(name = "compilationId") Long compilationId) {
        log.debug("LogMessages.TRY_ADMIN_DELETE_COMPILATIONS_ID.label, compId");
        adminCompilationsService.deleteCompilation(compilationId);
    }

    @PatchMapping(path = "/compilations/{compilationId}")
    public CompilationDto changeCompilations(@PathVariable(name = "compilationId") Long compilationId,
                                             @RequestBody @Valid CompilationUpdateRequestDto compilationUpdateRequestDto) {
        log.debug("LogMessages.TRY_ADMIN_PATCH_COMPILATIONS_ID.label, compilationId");
        return adminCompilationsService.changeCompilation(compilationId, compilationUpdateRequestDto);
    }
}