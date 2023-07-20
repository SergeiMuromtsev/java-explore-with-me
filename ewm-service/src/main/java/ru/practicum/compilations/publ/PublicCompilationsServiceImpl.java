package ru.practicum.compilations.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.repository.CompilationsRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.NotFoundException;
import java.util.List;

import static ru.practicum.compilations.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.compilations.mapper.CompilationMapper.toListCompilationsDto;
import static ru.practicum.utils.Page.paged;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationsServiceImpl implements PublicCompilationsService {
    private final CompilationsRepository compilationsRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilations(String pinned, int from, int size) {
        Pageable page = paged(from, size);
        return toListCompilationsDto(compilationsRepository.findCompilationsByPinnedIs(Boolean.parseBoolean(pinned), page));
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        return toCompilationDto(compilationsRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("no compilation with id " + compilationId)));
    }
}
