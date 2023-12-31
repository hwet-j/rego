package com.clipclap.rego.controller;

import com.clipclap.rego.model.dto.PlannerDTO;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.DetailPlanRepository;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.repository.UserRepository;
import com.clipclap.rego.service.AuthService;
import com.clipclap.rego.service.DetailPlanService;
import com.clipclap.rego.service.PlannerService;
import com.clipclap.rego.service.TouristAttractionService;
import com.clipclap.rego.validation.JoinForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class IndexController {

	private final UserRepository userRepository;
	private final AuthService authService;
	private final TouristAttractionService touristAttractionService;
	private final TouristAttractionRepository touristAttractionRepository;
	private final ObjectMapper objectMapper;
	private final DetailPlanRepository detailPlanRepository;
	private final DetailPlanService detailPlanService;
	private final PlannerService plannerService;

	// 로그인이 완료되면 실행되는 페이지로 수정 X
	// API로 로그인한 경우에 DB정보를 확인해서 회원가입한 이력이 있다면, 로그인 그렇지않으면 회원가입창으로 이동
	@GetMapping({ "", "/" })
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		List<PlannerDTO> recentPlanners = plannerService.findTop4RecentPlanners();
		model.addAttribute("findTop4RecentPlanners", recentPlanners);

		if (authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {

			Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());

			User user = optionalUser.get();
			// 비밀번호가 설정되어있지 않으면 로그인 불가
			if (user.getPassword() == null){
				String accessToken = null;

				model.addAttribute("user", user);
				model.addAttribute("joinForm", new JoinForm());	// 유효성 검사를 위한 Form 전달

				// 로그아웃 처리
				if (user != null) {
					new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
				}
				// 회원가입 창으로 이동
				return "login";
			}
			// 회원가입 이력이 존재
			String username = authentication.getName();



			return "main";
		} else {

			return "main";

		}
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
		// 비밀번호와 비밀번호 확인이 일치하지 않으면 에러 처리
		if (!form.getPassword().equals(form.getPasswordConfirm())) {
			bindingResult.rejectValue("passwordConfirm", "passwordConfirm", "비밀번호가 일치하지 않습니다.");
		}

		if (bindingResult.hasErrors()){
			model.addAttribute("user", userRepository.findByEmail(form.getEmail()).get());
			return "login";
		}

		/* 닉네임 중복 체크 */
		if (authService.nicknameDuplicateCheck(form.getNickname()).equals("Duplicate")){
			model.addAttribute("user", userRepository.findByEmail(form.getEmail()).get());
			bindingResult.addError(new FieldError("duplicate","nickname", "닉네임은 이미 사용중입니다."));
			return "login";
		}


		String result = authService.join(form);

		if (result.equals("password")){

		} else if (result.equals("id")){
			System.out.println();
		}

		return "redirect:/login";
	}


	/* 탈퇴 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/apiUnlink")
	public String apiUnlink(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient auth2AuthorizedClient) {

		String accessToken = null;

		if (auth2AuthorizedClient.getClientRegistration().getRegistrationId().equals("google")){
			accessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
			authService.googleUnlink(accessToken);
		} else if (auth2AuthorizedClient.getClientRegistration().getRegistrationId().equals("kakao")){
			accessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
			authService.kakaoUnlink(accessToken);
		}

		return "redirect:/";
	}







	@GetMapping("/findPw")
	public String findPw(){
		return "/findPw";
	}
}
