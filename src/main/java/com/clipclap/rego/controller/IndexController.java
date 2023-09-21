package com.clipclap.rego.controller;

import com.clipclap.rego.config.auth.PrincipalDetails;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.repository.UserRepository;
import com.clipclap.rego.service.AuthService;
import com.clipclap.rego.validation.JoinForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class IndexController {

	private final UserRepository userRepository;
	private final AuthService authService;
	private final TouristAttractionRepository touristAttractionRepository;


	// 로그인이 완료되면 실행되는 페이지로 수정 X
	// API로 로그인한 경우에 DB정보를 확인해서 회원가입한 이력이 있다면, 로그인 그렇지않으면 회원가입창으로 이동
	@GetMapping({ "", "/" })
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {

			User user = userRepository.findByUsername(authentication.getName());
			// 비밀번호가 설정되어있지 않으면 로그인 불가
			if (user.getPassword() == null){

				model.addAttribute("user", user);
				model.addAttribute("joinForm", new JoinForm());	// 유효성 검사를 위한 Form 전달

				// 로그아웃 처리
				if (user != null) {
					new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
				}
				// 회원가입 창으로 이동
				return "join";
			}
			// 회원가입 이력이 존재
			String username = authentication.getName();
			model.addAttribute("data", "로그인 ID : " + username);
			return "main";
		} else {
			// 사용자가 인증되지 않음
			model.addAttribute("data", "로그아웃 상태입니다.");
			return "main";
		}
	}

	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principal) {
		System.out.println("Principal : " + principal);
		System.out.println("OAuth2 : "+principal.getUser().getProvider());
		// iterator 순차 출력 해보기
		Iterator<? extends GrantedAuthority> iter = principal.getAuthorities().iterator();
		while (iter.hasNext()) {
			GrantedAuthority auth = iter.next();
			System.out.println(auth.getAuthority());
		}

		return "유저 페이지입니다.";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "어드민 페이지입니다.";
	}
	
	//@PostAuthorize("hasRole('ROLE_MANAGER')")
	//@PreAuthorize("hasRole('ROLE_MANAGER')")
	@Secured("ROLE_MANAGER")
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "매니저 페이지입니다.";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/join")
	public String join() {
		return "join";
	}

	@PostMapping("/joinProc")
	public String joinProc(@Valid JoinForm form,
						   BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()){
			model.addAttribute("user", userRepository.findByEmail(form.getEmail()));
			return "join";
		}

		String result = authService.join(form);

		if (result.equals("password")){

		} else if (result.equals("id")){
			System.out.println("");
		}

		return "redirect:/";
	}

	@GetMapping("/map")
	public String map() {
		return "googleMap";
	}

	@GetMapping("/map2")
	public String map2(Model model) {
		List<TouristAttraction> mallList = touristAttractionRepository.findAll();

		for (TouristAttraction items : mallList){
			System.out.println(items);
		}

		model.addAttribute("mallList" , mallList);

		return "googleMap2";
	}
}
