package com.example.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Memo;

// JUnit : 테스트 라이브러리 (starter 라이브러리 안에 포함되있다)

@SpringBootTest // 테스트 전용 클래스 라고 명시하는 어노테이션
public class MemoRepositoryTest {

    @Autowired // MemoRepository memoRepository = new MemoRepositoryImpl();
    private MemoRepository memoRepository;

    @Test // 테스트 메소드(리턴타입은 void,테스트메소드는 void 타입이다) 지정
    public void insertMemo() {
        // 100 개의 메모 입력
        for (int i = 1; i < 101; i++) {
            Memo memo = new Memo();
            memo.setMemoText("MemoText" + i);

            // save() : insert, update 작업할때 사용한다
            memoRepository.save(memo); // dao.insert()
        }
    }

    @Test
    public void getMemo() {
        // 특정 아이디 메모 조회
        // Optional : java.util, null 을 체크할수 있는 메소드를 보유하고있다
        Optional<Memo> result = memoRepository.findById(21L);

        // Optional 에 담고 .get() 으로 불러와야한다
        System.out.println(result.get());
    }

    @Test
    public void getMemoList() {
        // 전체 메모 조회
        // List : java.util, null 을 체크할수 있는 메소드를 보유하고있다
        List<Memo> result = memoRepository.findAll();
        for (Memo memo : result) {
            System.out.println(memo);
        }
    }

    @Test
    public void updateMemo() {
        // 먼저 업데이트 할 entity 찾기
        Optional<Memo> result = memoRepository.findById(25L);

        Memo memo = result.get();

        memo.setMemoText("update Title.....");
        // save() : 스냅샷으로 비교해서 달라진 부분이 있으면 알아서 update 해준다
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void deleteMemo() {
        // 삭제할 entity 찾기
        Optional<Memo> result = memoRepository.findById(24L);
        Memo memo = result.get();

        // 삭제
        memoRepository.delete(memo);
        // 삭제가 잘되면 : 삭제 memoOptional.empty
        System.out.println("삭제 memo" + memoRepository.findById(24L));
    }

    // 객체지향쿼리 테스트
    @Test
    public void queryMethodTest() {
        // 이거는 지금 mno 가 없어서 에러 뜸
        // List<Memo> list = memoRepository.findByMnoLessThan(5L);
        // list.forEach(memo -> System.out.println(memo));

        List<Memo> list = memoRepository.findByMnoLessThanOrderByMnoDesc(10L);
        list.forEach(memo -> System.out.println(memo));

        list = memoRepository.findByMnoBetween(50L, 70L);
        list.forEach(memo -> System.out.println(memo));
    }
}
