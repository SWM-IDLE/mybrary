package kr.mybrary.bookservice.book.persistence.repository;

import java.util.Optional;
import kr.mybrary.bookservice.book.persistence.translator.Translator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslatorRepository extends JpaRepository<Translator, Long> {

    Optional<Translator> findByName(String name);
}
