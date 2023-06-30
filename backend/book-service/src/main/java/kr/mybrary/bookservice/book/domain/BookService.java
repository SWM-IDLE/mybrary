package kr.mybrary.bookservice.book.domain;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.AuthorRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.book.persistence.author.Author;
import kr.mybrary.bookservice.book.persistence.author.BookAuthor;
import kr.mybrary.bookservice.book.persistence.repository.TranslatorRepository;
import kr.mybrary.bookservice.book.persistence.translator.BookTranslator;
import kr.mybrary.bookservice.book.persistence.translator.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final TranslatorRepository translatorRepository;

    public Book getRegisteredOrNewBook(BookCreateServiceRequest request) {
        return bookRepository.findByIsbn10OrIsbn13(request.getIsbn10(), request.getIsbn10())
                .orElseGet(() -> create(request));
    }

    private Book create(BookCreateServiceRequest request) {

        Book book = BookDtoMapper.INSTANCE.bookCreateRequestToEntity(request);

        List<BookAuthor> bookAuthors = request.getAuthors().stream()
                .map(this::getAuthor)
                .map(author -> BookAuthor.builder().author(author).build())
                .toList();

        List<BookTranslator> bookTranslators = request.getTranslators().stream()
                .map(this::getTranslator)
                .map(translator -> BookTranslator.builder().translator(translator).build())
                .toList();

        book.addBookAuthorAndBookTranslator(bookAuthors, bookTranslators);

        return bookRepository.save(book);
    }

    private Author getAuthor(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseGet(() -> Author.builder().name(authorName).build());
    }

    private Translator getTranslator(String translatorName) {
        return translatorRepository.findByName(translatorName)
                .orElseGet(() -> Translator.builder().name(translatorName).build());
    }
}
