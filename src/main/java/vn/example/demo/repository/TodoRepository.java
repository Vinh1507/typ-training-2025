package vn.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.example.demo.domain.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}