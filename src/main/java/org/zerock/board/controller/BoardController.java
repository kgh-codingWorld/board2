package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    // 클라이언트로부터의 HTTP 요청을 처리하고 응답을 반환
    // DTO를 사용하여 클라이언트와 데이터를 주고받음

    private final BoardService boardService; // final로 선언 -> Board만의 컨트롤러이기 때문에 Service도 고정해놓음

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("list..." + pageRequestDTO);

        model.addAttribute("result", boardService.getList(pageRequestDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/register")
    public void registerGet(){

    }

    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes rttr) {

        log.info("dto..." + dto);
        // 새로 추가된 엔티티 번호
        Long bno = boardService.register(dto);

        log.info("BNO: " + bno);

        rttr.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read", "/modify" })
    public void read(PageRequestDTO pageRequestDTO, Long bno, Model model){

        log.info("bno: " + bno);

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);

    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes rttr) {

        log.info("bno: " + bno);

        boardService.removeWithReplies(bno);

        rttr.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @PreAuthorize("principal.username == #boardDTO.writer") // principal.username: 현재 로그인된 사용자 아이디, #boardDTO: 현재 파라미터가 수집된 BoardDTO
    @PostMapping("/modify")
    public String modify(@Valid BoardDTO dto,
                         BindingResult bindingResult,
                         PageRequestDTO pageRequestDTO,
                         RedirectAttributes rttr) {

        log.info("post modify...");

        log.info("dto: " + dto);

        boardService.modify(dto);

        rttr.addAttribute("page", pageRequestDTO.getPage());
        rttr.addFlashAttribute("type", pageRequestDTO.getType());
        rttr.addFlashAttribute("keyword", pageRequestDTO.getKeyword());

        rttr.addFlashAttribute("bno", dto.getBno());

        return "redirect:/board/read";
    }
}
