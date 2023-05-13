package com.example.backend.repository;

import com.example.backend.model.Note;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note,Long> {
    Optional<Note> findByIdAndUser(Long id, User user);
    List<Note> findAllByUser(User user);
}
