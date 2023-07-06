package kr.mybrary.bookservice.mybook.domain;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookAlreadyExistsException;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MyBookService {

    private final MyBookRepository myBookRepository;
    private final BookService bookService;

    public MyBook create(MyBookCreateServiceRequest request) {

        Book book = bookService.getRegisteredBook(request.toBookCreateRequest());

        checkBookAlreadyRegisteredAsMyBook(request.getUserId(), book);

        MyBook myBook = MyBook.builder()
                .book(book)
                .userId(request.getUserId())
                .isDeleted(false)
                .build();

        return myBookRepository.save(myBook);
    }

    private void checkBookAlreadyRegisteredAsMyBook(String userId, Book book) {
        if (myBookRepository.existsByUserIdAndBook(userId, book)) {
            throw new MyBookAlreadyExistsException();
        }
    }

    public List<MyBookElementResponse> findAllMyBooks(String userId) {
        // TODO

        return List.of();
    }

    public MyBookDetailResponse findMyBookDetail(String userId, Long id) {
        // TODO

        return null;
    }

    public void deleteMyBook(String userId, Long id) {
        // TODO
    }
}
