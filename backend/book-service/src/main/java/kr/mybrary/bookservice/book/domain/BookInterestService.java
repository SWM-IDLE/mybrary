package kr.mybrary.bookservice.book.domain;

import java.util.function.Consumer;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestServiceRequest;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.repository.BookInterestRepository;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestHandleResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestHandleResponse.BookInterestHandleResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookInterestService {

    private final BookInterestRepository bookInterestRepository;
    private final BookReadService bookReadService;

    public BookInterestHandleResponse handleBookInterest(BookInterestServiceRequest request) {

        Book book = bookReadService.getRegisteredBookByISBN13(request.getIsbn13());
        BookInterestHandleResponseBuilder response = makeBookHandleResponse(request, book);

        bookInterestRepository.findByBookAndUserId(book, request.getLoginId())
                .ifPresentOrElse(
                        cancelBookInterest(book, response),
                        () -> {
                            registerBookInterest(request, book, response);
                        }
                );

        return response.build();
    }

    private BookInterestHandleResponseBuilder makeBookHandleResponse(BookInterestServiceRequest request, Book book) {
        return BookInterestHandleResponse.builder()
                .isbn13(book.getIsbn13())
                .userId(request.getLoginId());
    }

    private void registerBookInterest(BookInterestServiceRequest request, Book book, BookInterestHandleResponseBuilder response) {
        BookInterest bookInterest = BookInterest.builder()
                .userId(request.getLoginId())
                .book(book)
                .build();

        book.increaseInterestCount();
        bookInterestRepository.save(bookInterest);
        response.interested(true);
    }

    @NotNull
    private Consumer<BookInterest> cancelBookInterest(Book book, BookInterestHandleResponseBuilder response) {
        return bookInterest -> {
            bookInterestRepository.delete(bookInterest);
            book.decreaseInterestCount();
            response.interested(false);
        };
    }
}
