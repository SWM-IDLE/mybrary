package kr.mybrary.bookservice.book.domain;

import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookCreateServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookDetailServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.response.BookDetailServiceResponse;
import kr.mybrary.bookservice.book.domain.e.BookAlreadyExistsException;
import kr.mybrary.bookservice.book.domain.exception.BookNotFoundException;
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
import kr.mybrary.bookservice.booksearch.domain.PlatformBookSearchApiService;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final TranslatorRepository translatorRepository;
    private final BookCategoryRepository bookCategoryRepository;

    private final PlatformBookSearchApiService platformBookSearchApiService;

    @Transactional(readOnly = true)
    public BookDetailServiceResponse getBookDetailByISBN(BookDetailServiceRequest request) {

        return bookRepository.findByISBNWithAuthorAndCategoryUsingFetchJoin(request.getIsbn10(), request.getIsbn13())
                .map(BookDtoMapper.INSTANCE::bookToDetailServiceResponse)
                .orElseGet(() -> {
                    BookSearchDetailResponse bookSearchDetailResponse = platformBookSearchApiService.searchBookDetailWithISBN(
                            BookSearchServiceRequest.of(request.getIsbn13()));
                    return BookDtoMapper.INSTANCE.bookSearchDetailToDetailServiceResponse(bookSearchDetailResponse);
                });
    }

    @Transactional(readOnly = true)
    public Book getRegisteredBookByISBN13(String isbn13) {
        return bookRepository.findByIsbn13(isbn13).orElseThrow(BookNotFoundException::new);
    }

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
