package com.example.board.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.board.dto.ReplyDto;
import com.example.board.service.ReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Log4j2
@RequestMapping("/replies")
@RestController
public class ReplyController {

    private final ReplyService service;

    // http://localhost:8080/replies/board/100
    @GetMapping("/board/{bno}")
    public List<ReplyDto> getListByBoard(@PathVariable("bno") Long bno) {
        log.info("replies List get {}", bno);
        List<ReplyDto> replies = service.getReplies(bno);

        // @RestController 는 데이터를 리턴한다 (페이지 리턴x)
        return replies;
    }

    // /replies/new + post
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/new")
    public ResponseEntity<Long> postCreate(@RequestBody ReplyDto dto) {
        log.info("댓글등록 {}", dto);

        return new ResponseEntity<>(service.create(dto), HttpStatus.OK);
    }

    // /replies/{rno} + delete
    @DeleteMapping("/{rno}")
    public ResponseEntity<String> deleteReply(@PathVariable("rno") Long rno) {
        log.info("댓글 삭제 {}", rno);

        service.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // /replies/{rno} + get
    @GetMapping("/{rno}")
    public ResponseEntity<ReplyDto> getRow(@PathVariable("rno") Long rno) {
        log.info("댓글 하나 요청 {}", rno);
        return new ResponseEntity<>(service.getReply(rno), HttpStatus.OK);
    }

    // 댓글수정 : /replies/{rno} + put
    @PreAuthorize("authentication.name == #dto.writerEmail")
    @PutMapping("/{rno}")
    public ResponseEntity<Long> putReply(@PathVariable("rno") Long rno, @RequestBody ReplyDto dto) {
        log.info("댓글수정 요청 rno, dto {}, {}", rno, dto);

        rno = service.update(dto);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }
    // @PutMapping("/{id}")
    // public ResponseEntity<String> putReply(@PathVariable("id") String id,
    // @RequestBody ReplyDto dto) {
    // log.info("댓글수정 요청 {}, {}", id, dto);
    // Long rno = service.update(dto);
    // return new ResponseEntity<>(String.valueOf(rno), HttpStatus.OK);
    // }

}
