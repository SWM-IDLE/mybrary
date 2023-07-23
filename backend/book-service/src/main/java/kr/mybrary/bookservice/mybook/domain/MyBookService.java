package kr.mybrary.bookservice.mybook.domain;

import java.util.List;
import kr.mybrary.bookservice.book.domain.BookService;
import kr.mybrary.bookservice.book.persistence.Book;
import kr.mybrary.bookservice.mybook.domain.dto.MyBookDtoMapper;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookCreateServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookDeleteServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookDetailServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookFindAllServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MyBookFindByMeaningTagQuoteServiceRequest;
import kr.mybrary.bookservice.mybook.domain.dto.request.MybookUpdateServiceRequest;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookAccessDeniedException;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookAlreadyExistsException;
import kr.mybrary.bookservice.mybook.domain.exception.MyBookNotFoundException;
import kr.mybrary.bookservice.mybook.persistence.MyBook;
import kr.mybrary.bookservice.mybook.persistence.repository.MyBookRepository;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookDetailResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookElementResponse;
import kr.mybrary.bookservice.mybook.presentation.dto.response.MyBookUpdateResponse;
import kr.mybrary.bookservice.tag.domain.MeaningTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyBookService {

    private final MyBookRepository myBookRepository;
    private final MeaningTagService meaningTagService;
    private final BookService bookService;

    public MyBook create(MyBookCreateServiceRequest request) {

        Book book = bookService.getRegisteredBook(request.toBookCreateRequest());
        checkBookAlreadyRegisteredAsMyBook(request.getUserId(), book);

        MyBook myBook = MyBook.of(book, request.getUserId());
        return myBookRepository.save(myBook);
    }

    private void checkBookAlreadyRegisteredAsMyBook(String userId, Book book) {
        if (myBookRepository.existsByUserIdAndBook(userId, book)) {
            throw new MyBookAlreadyExistsException();
        }
    }

    @Transactional(readOnly = true)
    public List<MyBookElementResponse> findAllMyBooks(MyBookFindAllServiceRequest request) {

        List<MyBook> mybooks = myBookRepository.findAllByUserId(request.getUserId());

        return mybooks.stream()
                .filter(myBook -> !myBook.isDeleted())
                .filter(myBook -> request.getUserId().equals(request.getLoginId()) || myBook.isShowable())
                .map(MyBookDtoMapper.INSTANCE::entityToMyBookElementResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MyBookDetailResponse findMyBookDetail(MyBookDetailServiceRequest request) {

        MyBook myBook = findMyBookById(request.getMybookId());

        if (myBook.isPrivate() && isOwnerSameAsRequester(myBook.getUserId(), request.getLoginId())) {
            throw new MyBookAccessDeniedException();
        }

        return MyBookDtoMapper.INSTANCE.entityToMyBookDetailResponse(myBook);
    }

    public void deleteMyBook(MyBookDeleteServiceRequest request) {

        MyBook myBook = findMyBookById(request.getMybookId());

        if (isOwnerSameAsRequester(myBook.getUserId(), request.getLoginId())) {
            throw new MyBookAccessDeniedException();
        }

        myBook.deleteMyBook();
    }

    public MyBookUpdateResponse updateMyBookProperties(MybookUpdateServiceRequest request) {

        MyBook myBook = findMyBookById(request.getMyBookId());

        if (isOwnerSameAsRequester(myBook.getUserId(), request.getLoginId())) {
            throw new MyBookAccessDeniedException();
        }

        myBook.updateFromUpdateRequest(request);
        meaningTagService.assignMeaningTag(request.toMeaningTagAssignServiceRequest(myBook));

        return MyBookUpdateResponse.of(myBook, request.getMeaningTag());
    }

    private static boolean isOwnerSameAsRequester(String ownerId, String requesterId) {
        return !ownerId.equals(requesterId);
    }

    private MyBook findMyBookById(Long myBookId) {
        return myBookRepository.findByIdAndDeletedIsFalse(myBookId)
                .orElseThrow(MyBookNotFoundException::new);
    }

    public List<MyBookElementResponse> findByMeaningTagQuote(
            MyBookFindByMeaningTagQuoteServiceRequest request) {

        return myBookRepository.findByMeaningTagQuote(request.getQuote())
                .stream()
                .map(MyBookDtoMapper.INSTANCE::entityToMyBookElementResponse)
                .toList();
    }
}
