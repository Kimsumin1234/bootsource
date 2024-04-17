package com.example.board.service;

import java.util.List;

import com.example.board.dto.BoardDto;
import com.example.board.dto.PageRequestDto;
import com.example.board.dto.PageResultDto;
import com.example.board.entity.Board;
import com.example.board.entity.Member;

public interface BoardService {
    // 페이지 나누기 전
    // List<BoardDto> getList(PageRequestDto requestDto);

    // 페이지 나누기 후
    // Object[] : entity 가 3개 여서 이걸 사용
    PageResultDto<BoardDto, Object[]> getList(PageRequestDto requestDto);

    BoardDto getRow(Long bno);

    void update(BoardDto updateDto);

    void removeWithReplies(Long bno);

    // entityToDto
    // BoardRepositoryTest 에 objects[0],[1],[2] 순서로 변수입력
    public default BoardDto entityToDto(Board board, Member member, Long replyCount) {
        return BoardDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount != null ? replyCount : 0)
                .createdDate(board.getCreatedDate())
                .lastModifiedDate(board.getLastModifiedDate())
                .build();

    }

    // dtoToEntity
    public default Board dtoToEntity(BoardDto dto) {
        Member member = Member.builder().email(dto.getWriterEmail()).build();
        return Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
    }
}
