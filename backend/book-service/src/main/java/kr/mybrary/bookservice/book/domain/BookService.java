package kr.mybrary.bookservice.book.domain;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.BookCategory;
import kr.mybrary.bookservice.book.persistence.repository.AuthorRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookCategoryRepository;
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
    private final BookCategoryRepository bookCategoryRepository;

    public Book getRegisteredBook(BookCreateServiceRequest request) {
        return bookRepository.findByIsbn10OrIsbn13(request.getIsbn10(), request.getIsbn13())
                .orElseGet(() -> create(request));
    }

    private Book create(BookCreateServiceRequest request) {

        Book book = BookDtoMapper.INSTANCE.bookCreateRequestToEntity(request);

        List<BookAuthor> bookAuthors = request.getAuthors().stream()
                .map(r -> getAuthor(r.getAuthorId(), r.getName()))
                .map(author -> BookAuthor.builder().author(author).build())
                .toList();

        List<BookTranslator> bookTranslators = request.getTranslators().stream()
                .map(r -> getTranslator(r.getTranslatorId(), r.getName()))
                .map(translator -> BookTranslator.builder().translator(translator).build())
                .toList();

        book.addBookAuthor(bookAuthors);
        book.addBookTranslator(bookTranslators);
        book.assignCategory(getBookCategory(request.getCategoryId(), request.getCategory()));

        return bookRepository.save(book);
    }

    private Author getAuthor(Integer authorId, String authorName) {
        return authorRepository.findByAid(authorId)
                .orElseGet(() -> Author.builder().aid(authorId).name(authorName).build());
    }

    private Translator getTranslator(Integer translatorId, String translatorName) {
        return translatorRepository.findByTid(translatorId)
                .orElseGet(() -> Translator.builder().tid(translatorId).name(translatorName).build());
    }

    private BookCategory getBookCategory(Integer categoryId, String categoryName) {
        return bookCategoryRepository.findByCid(categoryId)
                .orElseGet(() -> BookCategory.builder().cid(categoryId).name(categoryName).build());
    }
}
