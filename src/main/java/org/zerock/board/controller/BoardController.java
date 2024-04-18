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
import org.zerock.board.dto.PageResponseDTO;
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

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/register")
    public void registerGET() {

    }

    @PostMapping("/register")
    public String registerPOST(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes rttr) {
        if(bindingResult.hasErrors()) {
            log.info("has errors...");
            rttr.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);

        rttr.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

}
