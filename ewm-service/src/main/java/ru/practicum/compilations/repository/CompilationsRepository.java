package ru.practicum.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.compilations.model.Compilation;

import java.util.List;

@Repository
public interface CompilationsRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findCompilationsByPinnedIs(boolean pinned, Pageable page);
}

