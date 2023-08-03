package kr.mybrary.bookservice.book.persistence.repository;

import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.bookInfo.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String name);

    Optional<Author> findByAid(Integer aid);
}
