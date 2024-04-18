package com.example.board.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.dto.BoardDto;
import com.example.board.dto.PageRequestDto;
import com.example.board.dto.PageResultDto;
import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.MemberRepository;
import com.example.board.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final MemberRepository memberRepository;

    // 페이지 나누기 전
    // @Override
    // public List<BoardDto> getList(PageRequestDto requestDto) {

    // List<Object[]> result = boardRepository.list();

    // Function<Object[], BoardDto> fn = (entity -> entityToDto((Board) entity[0],
    // (Member) entity[1],
    // (Long) entity[2]));
    // return result.stream().map(fn).collect(Collectors.toList());
    // }

    // 페이지 나누기 후 + 검색 (requestDto.getType(), requestDto.getKeyword())
    @Override
    public PageResultDto<BoardDto, Object[]> getList(PageRequestDto requestDto) {

        Page<Object[]> result = boardRepository.list(requestDto.getType(), requestDto.getKeyword(),
                requestDto.getPageable(Sort.by("bno").descending()));

        Function<Object[], BoardDto> fn = (entity -> entityToDto((Board) entity[0],
                (Member) entity[1],
                (Long) entity[2]));
        return new PageResultDto<>(result, fn);
    }

    @Override
    public BoardDto getRow(Long bno) {
        Object[] row = boardRepository.getRow(bno);

        return entityToDto((Board) row[0], (Member) row[1], (Long) row[2]);
    }

    @Override
    public void update(BoardDto updateDto) {
        Board board = boardRepository.findById(updateDto.getBno()).get();
        board.setTitle(updateDto.getTitle());
        board.setContent(updateDto.getContent());
        boardRepository.save(board);

    }

    // Reply 와 Board 가 묶여있다 둘중에 하나라도 실패가 나면 원상복구 해야해서
    // @Transactional 을 사용해서 두개의 테이블을 하나의 작업으로 만들어준다
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        replyRepository.deldeleteByBno(bno);
        boardRepository.deleteById(bno);
    }

    @Override
    public Long create(BoardDto createDto) {
        Optional<Member> member = memberRepository.findById(createDto.getWriterEmail());

        if (member.isPresent()) {
            Board entity = Board.builder()
                    .title(createDto.getTitle())
                    .content(createDto.getContent())
                    .writer(member.get())
                    .build();
            entity = boardRepository.save(entity);
            return entity.getBno();
        }
        return null;
    }

}
