package com.example.board.service;

import java.util.List;

import com.example.board.dto.ReplyDto;
import com.example.board.entity.Board;
import com.example.board.entity.Reply;

public interface ReplyService {

    List<ReplyDto> getReplies(Long bno);

    Long create(ReplyDto dto);

    void remove(Long rno);

    ReplyDto getReply(Long rno);

    Long update(ReplyDto dto);

    // 이거 바꿔주는 메소드들이 라이브러리에 있다
    // entityToDto
    public default ReplyDto entityToDto(Reply reply) {
        return ReplyDto.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .bno(reply.getBoard().getBno())
                .createdDate(reply.getCreatedDate())
                .lastModifiedDate(reply.getLastModifiedDate())
                .build();

    }

    // dtoToEntity
    public default Reply dtoToEntity(ReplyDto dto) {
        Board board = Board.builder().bno(dto.getBno()).build();
        return Reply.builder()
                .rno(dto.getRno())
                .text(dto.getText())
                .replyer(dto.getReplyer())
                .board(board)
                .build();
    }
}
