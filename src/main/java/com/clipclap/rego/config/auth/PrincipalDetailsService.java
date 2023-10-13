package com.clipclap.rego.config.auth;

import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	// 일반 로그인을 진행하면 실행되는 메서드 -> API 로그인은 실행되지 않음
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 이메일로 검색

		Optional<User> user = userRepository.findByEmail(username);

		if (!user.isPresent()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
		} else {
			return new PrincipalDetails(user.get());
		}
		
	}

}
