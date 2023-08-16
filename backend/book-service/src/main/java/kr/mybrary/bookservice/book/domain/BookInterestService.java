package kr.mybrary.bookservice.book.domain;

import java.util.List;
import kr.mybrary.bookservice.book.domain.dto.BookDtoMapper;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookInterestStatusServiceRequest;
import kr.mybrary.bookservice.book.domain.dto.request.BookMyInterestFindServiceRequest;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.book.persistence.BookInterest;
import kr.mybrary.bookservice.book.persistence.repository.BookInterestRepository;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestElementResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestHandleResponse;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestHandleResponse.BookInterestHandleResponseBuilder;
import kr.mybrary.bookservice.book.presentation.dto.response.BookInterestStatusResponse;
import lombok.RequiredArgsConstructor;
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
        BookInterestHandleResponseBuilder response = makeBookHandleResponse(request.getLoginId(), book.getIsbn13());

        bookInterestRepository.findByBookAndUserId(book, request.getLoginId())
                .ifPresentOrElse(
                        bookInterest -> {
                            cancelBookInterest(book, bookInterest);
                            response.interested(false);
                        },
                        () -> {
                            registerBookInterest(book, request.getLoginId());
                            response.interested(true);
                        }
                );

        return response.build();
    }

    @Transactional(readOnly = true)
    public List<BookInterestElementResponse> getBookInterestList(BookMyInterestFindServiceRequest request) {

        return bookInterestRepository.findAllByUserIdWithBook(request.getLoginId(), request.getBookOrderType())
                .stream()
                .map(BookDtoMapper.INSTANCE::bookInterestToBookInterestElementResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookInterestStatusResponse getInterestStatus(BookInterestStatusServiceRequest request) {
        return bookReadService.findOptionalBookByISBN13(request.getIsbn13())
                .map(book -> BookInterestStatusResponse.of(
                        bookInterestRepository.existsByBookAndUserId(book, request.getLoginId())))
                .orElseGet(() -> BookInterestStatusResponse.of(false));
    }

    private BookInterestHandleResponseBuilder makeBookHandleResponse(String loginId, String isbn13) {
        return BookInterestHandleResponse.builder()
                .isbn13(isbn13)
                .userId(loginId);
    }

    private void registerBookInterest(Book book, String loginId) {
        BookInterest bookInterest = BookInterest.builder()
                .userId(loginId)
                .book(book)
                .build();

        book.increaseInterestCount();
        bookInterestRepository.save(bookInterest);
    }

    private void cancelBookInterest(Book book, BookInterest bookInterest) {
        bookInterestRepository.delete(bookInterest);
        book.decreaseInterestCount();
    }
}