package kr.mybrary.bookservice.mybook.domain;

import jakarta.transaction.Transactional;
import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookAlreadyExistsException;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MyBookService {

    private final MyBookRepository myBookRepository;
    private final BookService bookService;

    public void create(MyBookCreateServiceRequest request) {

        Book book = bookService.getRegisteredOrNewBook(request.toBookCreateRequest());

        checkBookAlreadyRegisteredAsMyBook(request.getUserId(), book);

        MyBook myBook = MyBook.builder()
                .book(book)
                .userId(request.getUserId())
                .isDeleted(false)
                .build();

        myBookRepository.save(myBook);
    }

    private void checkBookAlreadyRegisteredAsMyBook(String userId, Book book) {
        if (myBookRepository.existsByUserIdAndBookId(userId, book.getId())) {
            throw new MyBookAlreadyExistsException();
        }
    }
}
