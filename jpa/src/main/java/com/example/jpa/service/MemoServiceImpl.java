package com.example.jpa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.dto.MemoDto;
import com.example.jpa.entity.Memo;
import com.example.jpa.repository.MemoRepository;

import lombok.RequiredArgsConstructor;

// 변수에 final 을 안쓰고 하는 방법
// @Autowired : 객체 주입
// @Service
// public class MemoServiceImpl {
//     @Autowired
//     private MemoRepository memoRepository;
// }

@Service // 서비스 클래스위에 설정 / 스프링 컨테이너가 해당 클래스의 객체를 생성한후 관리
@RequiredArgsConstructor // @NotNull, final 이 붙은 멤버변수를 대상으로 생성자 생성, 생성자가 있으면 스프링에서 객체를 알아서 주입해준다
public class MemoServiceImpl {
    private final MemoRepository memoRepository;

    public List<MemoDto> getMemoList() {
        // Entity 로 데이터를 받음
        List<Memo> entites = memoRepository.findAll();

        // Entity 를 Dto 모양으로 바꿔서 list 에 담는 작업
        List<MemoDto> list = new ArrayList<>();
        entites.forEach(entity -> list.add(entityToDto(entity)));
        return list;
    }

    public MemoDto getMemo(Long mno) {
        // Entity 로 데이터를 받음
        Memo entity = memoRepository.findById(mno).get();

        // Entity 를 Dto 모양으로 바꿔서 작업
        return entityToDto(entity);
    }

    public MemoDto updateMemo(MemoDto mDto) {
        // 업데이트 대상을 먼저 찾기
        Memo entity = memoRepository.findById(mDto.getMno()).get();

        // 변경
        entity.setMemoText(mDto.getMemoText());

        return entityToDto(memoRepository.save(entity));
    }

    public void deleteMemo(Long mno) {
        // 삭제 1번방법
        // Memo entity = memoRepository.findById(mno).get();
        // memoRepository.delete(entity);

        // 삭제 2번방법
        memoRepository.deleteById(mno);
    }

    public void insertMemo(MemoDto mDto) {
        // dto ==> entity 로 바꿔서 저장한다
        memoRepository.save(dtoToEntity(mDto));
    }

    // Entity 에 있는걸 Dto 로 옮기는 메소드
    private MemoDto entityToDto(Memo entity) {
        MemoDto mDto = MemoDto.builder()
                .mno(entity.getMno())
                .memoText(entity.getMemoText())
                .build();

        return mDto;
    }

    // Dto 에 있는걸 Entity 로 옮기는 메소드
    private Memo dtoToEntity(MemoDto mDto) {
        Memo entity = Memo.builder()
                .mno(mDto.getMno())
                .memoText(mDto.getMemoText())
                .build();

        return entity;
    }
}
