package com.example.board.repository.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.board.entity.Board;
import com.example.board.entity.QBoard;
import com.example.board.entity.QMember;
import com.example.board.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    // 페이지 나누기 전
    // @Override
    // public List<Object[]> list() {
    // log.info("Board + Reply + Member join");

    // // Q 클래스 사용
    // QBoard board = QBoard.board;
    // QMember member = QMember.member;
    // QReply reply = QReply.reply;

    // // @Query("select b,m from Board b left join b.writer m")
    // JPQLQuery<Board> query = from(board);
    // query.leftJoin(board.writer, member);

    // // 서브쿼리 작성 => JPAExpressions 를 사용
    // JPQLQuery<Long> replyCount =
    // JPAExpressions.select(reply.rno.count().as("replycnt")).from(reply)
    // .where(reply.board.eq(board)).groupBy(reply.board);
    // JPQLQuery<Tuple> tuple = query.select(board, member, replyCount);

    // List<Tuple> result = tuple.fetch();
    // List<Object[]> list = result.stream().map(t ->
    // t.toArray()).collect(Collectors.toList());
    // return list;
    // }

    // 페이지 나누기 후 + 검색(String type, String keyword)
    @Override
    public Page<Object[]> list(String type, String keyword, Pageable pageable) {
        log.info("Board + Reply + Member join");

        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(board.writer, member);

        JPQLQuery<Long> replyCount = JPAExpressions.select(reply.rno.count().as("replycnt")).from(reply)
                .where(reply.board.eq(board)).groupBy(reply.board);
        JPQLQuery<Tuple> tuple = query.select(board, member, replyCount);

        // 검색 (GuestBookServiceImpl 에서 getSearch 메소드 참고)
        BooleanBuilder builder = new BooleanBuilder();

        // 부담을 줄이기 위해 PK 를 이용해서 조건 주기 : bno > 0
        builder.and(board.bno.gt(0L));

        // Q 클래스 사용 (위에 있다)

        // 사용자가 입력한 type, keyword 가져오기 (메소드 변수로 받음)

        // 검색 타입이 있는 경우
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if (type.contains("t")) {
            conditionBuilder.or(board.title.contains(keyword));
        }
        if (type.contains("c")) {
            conditionBuilder.or(board.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(member.email.contains(keyword));
        }
        builder.and(conditionBuilder);

        tuple.where(builder);

        // 페이지 나누기
        // Pageable pageable = requestDto.getPageable(Sort.by("gno").descending()); 이게
        // 바로 안된다
        // sort 지정 코드
        Sort sort = pageable.getSort(); // 여러개의 테이블 sort 방식을 다 가져옴
        sort.stream().forEach(order -> {
            // 정렬방식이 ASC 인지 DESC 인지 지정
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();

            PathBuilder<Board> orderByExpression = new PathBuilder<>(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        // 페이지 처리 코드
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        // 게시글 전체 개수
        long count = tuple.fetchCount();

        List<Object[]> list = result.stream().map(t -> t.toArray()).collect(Collectors.toList());
        // 여기서는 pageable 에 다 안담겨서 3개로 나눠서 페이지정보를 보낸다
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Object[] getRow(Long bno) {
        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(board.writer, member);
        query.where(board.bno.eq(bno)); // where 절 추가함

        JPQLQuery<Long> replyCount = JPAExpressions.select(reply.rno.count().as("replycnt")).from(reply)
                .where(reply.board.eq(board)).groupBy(reply.board);
        JPQLQuery<Tuple> tuple = query.select(board, member, replyCount);

        Tuple result = tuple.fetch().get(0); // 1개만 가져오기 때문에

        return result.toArray();
    }

}
