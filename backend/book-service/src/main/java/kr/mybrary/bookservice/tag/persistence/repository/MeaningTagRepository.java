package kr.mybrary.bookservice.tag.persistence.repository;

import java.util.Optional;
import kr.mybrary.bookservice.tag.persistence.MeaningTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeaningTagRepository extends JpaRepository<MeaningTag, Long> {

    Optional<MeaningTag> findByQuote(String quote);

    Page<MeaningTag> findAllByOrderByRegisteredCountDesc(Pageable pageable);
}
