/**
 * Spring MVC : Controller integrated with Thymeleaf (View tech).
 *  refer: https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html
 * Thymeleaf parses the *.html template and evaluates the th:text expression
 * to render the value of the ${name} parameter that was set in the controller
 * */
package com.unistar.myservice2.controllers;

import com.unistar.myservice2.services.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Greeting2Controller {

	private final HelloService service;

	public Greeting2Controller(HelloService service) {
		this.service = service;
	}

	@RequestMapping("/hello")
	public @ResponseBody String greeting() {
		return service.hello();
	}

	@GetMapping("/greeting2")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting2";  // to find the "templates/greeting2.html"
	}

	@GetMapping("/searchPeople")
	public String searchPeople(@RequestParam(name="name", required=false, defaultValue="") String name, Model model) {
		model.addAttribute("name", name);
		return "searchPeople";  // to find the "templates/searchPeople.html"
	}
}
