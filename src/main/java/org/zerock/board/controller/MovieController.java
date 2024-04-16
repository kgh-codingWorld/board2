package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.MovieDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.MovieService;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController { // post 방식으로 전달된 파라미터들을 MovieDTO로 수집해서 MovieService 타입 객체의 register()를 호출

    private final MovieService movieService;

    @GetMapping("/register")
    public void register(){

    }

    // 영화 등록
    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes rttr) {
        log.info("movieDTO: " + movieDTO);

        Long mno = movieService.register(movieDTO);

        rttr.addFlashAttribute("msg", mno);

        return "redirect:/movie/list"; // 영화 등록 후 목록 페이지로 이동
    }

    // 영화 리스트
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("pageRequestDTO: " + pageRequestDTO);

        model.addAttribute("result", movieService.getList(pageRequestDTO));
    }

    @GetMapping({"/read", "/modify"})
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model ){

        log.info("mno: " + mno);

        MovieDTO movieDTO = movieService.getMovie(mno);

        model.addAttribute("dto", movieDTO);

    }
}
