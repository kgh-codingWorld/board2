package org.zerock.board.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@RestController // 기본이 json으로 처리한다.
@RestController
@RequestMapping("/api/sample") // http://localhost/api/sample/?????
@Log4j2
public class SampleController {

//    @GetMapping("/hello")
//    public void hello(Model model) {
//        log.info("hello...");
//        model.addAttribute("msg", "HELLO WORLD");
//    }

    @ApiOperation("Sample GET doA")
    @GetMapping("/doA")
    public List<String> doA(){
        return Arrays.asList("AAA","BBB","CCC");
    }

}

