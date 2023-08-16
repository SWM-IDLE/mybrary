package kr.mybrary.bookservice.book.domain;

import java.util.Optional;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookDetailServiceRequest;
import kr.mybrary.bookservice.book.domain.exception.BookNotFoundException;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.repository.BookRepository;
import kr.mybrary.bookservice.book.presentation.dto.response.BookDetailResponse;
import kr.mybrary.bookservice.booksearch.domain.PlatformBookSearchApiService;
import kr.mybrary.bookservice.booksearch.domain.dto.request.BookSearchServiceRequest;
import kr.mybrary.bookservice.booksearch.presentation.dto.response.BookSearchDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookReadService {

    private final BookRepository bookRepository;
    private final PlatformBookSearchApiService platformBookSearchApiService;
    private final BookWriteService bookWriteService;

    public BookDetailResponse getBookDetailByISBN(BookDetailServiceRequest request) {

        return bookRepository.findByISBNWithAuthorAndCategoryUsingFetchJoin(request.getIsbn10(), request.getIsbn13())
                .map(BookDtoMapper.INSTANCE::bookToDetailServiceResponse)
                .orElseGet(() -> {
                    BookSearchDetailResponse bookSearchDetailResponse = platformBookSearchApiService.searchBookDetailWithISBN(
                            BookSearchServiceRequest.of(request.getIsbn13()));

                    bookWriteService.create(BookDtoMapper.INSTANCE.bookSearchDetailToBookCreateServiceRequest(bookSearchDetailResponse));
                    return BookDtoMapper.INSTANCE.bookSearchDetailToDetailServiceResponse(bookSearchDetailResponse);
                });
    }

    public Book getRegisteredBookByISBN13(String isbn13) {
        return bookRepository.findByIsbn13(isbn13).orElseThrow(BookNotFoundException::new);
    }

    public Optional<Book> findOptionalBookByISBN13(String isbn13) {
        return bookRepository.findByIsbn13(isbn13);
    }
}
