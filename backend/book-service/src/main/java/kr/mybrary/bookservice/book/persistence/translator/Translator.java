package kr.mybrary.bookservice.book.persistence.translator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.mybrary.bookservice.global.BaseEntity;
import lombok.Getter;

@Entity
@Table(name = "translators")
@Getter
public class Translator extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
