package kr.mybrary.bookservice.book.domain;

import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.domain.exception.BookAlreadyExistsException;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookCategory;
import kr.mybrary.bookservice.book.persistence.bookInfo.Author;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookAuthor;
import kr.mybrary.bookservice.book.persistence.repository.AuthorRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookCategoryRepository;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.book.persistence.repository.TranslatorRepository;
import kr.mybrary.bookservice.book.persistence.bookInfo.BookTranslator;
import kr.mybrary.bookservice.book.persistence.bookInfo.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookWriteService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final TranslatorRepository translatorRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(BookCreateServiceRequest request) {

        checkBookAlreadyRegistered(request);
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

        bookRepository.save(book);
    }

    private void checkBookAlreadyRegistered(BookCreateServiceRequest request) {
        bookRepository.findByIsbn10OrIsbn13(request.getIsbn10(), request.getIsbn13())
                .ifPresent(book -> {
                    throw new BookAlreadyExistsException();
                });
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
