package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.GuestBookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.GuestBookService;

@Controller // 스프링에게..나 컨트롤러야
@RequestMapping("/guestbook") //http://localhost/guestbook/???
@Log4j2 // 로그 찍어봐
@RequiredArgsConstructor // 자동 주입을 위한 Annotation
public class GuestBookController { // 방명록에 관한 작업 처리용 메서드

    private final GuestBookService service; // final로 선언 -> GuestBook만의 컨트롤러이기 때문에 Service도 고정해놓음

    //---------------------------목록----------------------------
    @GetMapping("/") // 메인 페이지로 이동
    public String index() {
        return "redirect:/guestbook/list";
    } // 방명록 목록 페이지로 이동

    @GetMapping( "/list") // 방명록 목록 페이지로 이동
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("GuestBookController.list 메서드 실행" + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO)); // 검색에 관한 메서드 값 생성
    }
    //---------------------------등록----------------------------
    @GetMapping("/register") // 방명록 등록 페이지로 이동
    public void register(){ log.info("register get..."); }

    @PostMapping("/register") // 방명록 등록 페이지에서의 동작 처리
    public String registerPost(GuestBookDTO dto, RedirectAttributes rttr) {
        log.info("dto..." + dto);

        Long gno = service.register(dto); // GuestBook의 서비스 계층에서 등록 메서드를 불러옴

        rttr.addFlashAttribute("msg", gno); // 한 번만 msg라는 변수를 사용(모달)

        return "redirect:/guestbook/list"; // 처리 후 목록 화면으로 이동
    }

    //---------------------------조회 및 수정----------------------------
    //@GetMapping("/read")
    @GetMapping({"/read", "/modify"}) // 방명록 정보를 가지고 방명록 상세조회 또는 방명록 수정으로 이동
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        // PageRequestDTO: 방명록 검색 처리, 실제로 사용되는 모델 객체의 유형
        // @ModelAttribute: 메서드가 뷰로 전달할 모델 객체 정의
        // requestDTO: 모델 객체를 식별하는 이름
        // PageRequestDTO의 이름은 해당 DTO가 페이지 요청과 관련된 데이터를 포함한다는 것을 나타낼 수 있음

        log.info("gno" + gno);

        GuestBookDTO dto = service.read(gno); // 방명록 글번호로 해당 방명록에 대한 정보를 불러오는 메서드를 GuestBookDTO(방명록) 타입의 변수에 넣음

        model.addAttribute("dto", dto); // 방명록 정보를 담은 dto 변수를 model에 담음
    }

    //---------------------------삭제----------------------------
    @PostMapping("/remove")
    public String reomve(long gno, RedirectAttributes rttr) {

        log.info("gno: " + gno);

        service.remove(gno);

        rttr.addFlashAttribute("msg", gno); // 한 번만 msg라는 변수를 사용(모달)

        return "redirect:/guestbook/list";
    }

    //---------------------------수정----------------------------
    @PostMapping("/modify")
    public String modify(GuestBookDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes rttr){
        // GuestBookDTO: 수정해야 하는 글의 정보를 가짐
        // PageRequestDTO: 기존의 페이지 정보를 유지함
        // RedirectAttributes: 리다이렉트로 이동하기 위해 사용함
        log.info("post modify.............");
        log.info("dto: " + dto);

        service.modify(dto);

        rttr.addAttribute("page", requestDTO.getPage());
        rttr.addAttribute("gno", requestDTO.getType());
        rttr.addAttribute("gno", requestDTO.getKeyword());
        rttr.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read"; // 수정 작업 후 조회 페이지로 이동, 기존 페이지 정보도 같이 유지하여 조회 페이지에서 다시 목록 페이지로 이동하는데 어려움 없게 함
    }
}
