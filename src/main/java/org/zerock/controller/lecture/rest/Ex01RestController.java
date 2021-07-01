package org.zerock.controller.lecture.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest01/*")
public class Ex01RestController {

	@RequestMapping("/sub01")
	@ResponseBody
	public String method01() {
		return "/rest01/sub01";
		//view 로 해석이 됨.
	}
	
	@RequestMapping("/sub02")
	@ResponseBody
	public String method2() {
		return "hello world";
	}
	
	@RequestMapping("/sub03")
	public String method03() {
		return "someViewName";
	}
}
