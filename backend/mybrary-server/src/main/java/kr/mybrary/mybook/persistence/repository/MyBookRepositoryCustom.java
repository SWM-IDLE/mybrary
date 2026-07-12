package kr.mybrary.mybook.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import kr.mybrary.mybook.persistence.MyBookOrderType;
import kr.mybrary.mybook.persistence.ReadStatus;
import kr.mybrary.mybook.persistence.model.MyBookListDisplayElementModel;

public interface MyBookRepositoryCustom {

    List<MyBookListDisplayElementModel> findMyBookListDisplayElementModelsByUserId(String userId, MyBookOrderType myBookOrderType, ReadStatus readStatus);

    Long getBookRegistrationCountOfDay(LocalDate date);
}
