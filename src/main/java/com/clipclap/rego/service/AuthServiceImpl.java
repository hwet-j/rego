package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.AdminUserDTO;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.UserRepository;
import com.clipclap.rego.validation.JoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;
    private final RestTemplate restTemplate;


    @Override
    public String join(JoinForm joinForm) {
        Optional<User> optionalUser = userRepository.findByEmail(joinForm.getEmail());

        User apiInfo =optionalUser.get();
        String rawPassword = joinForm.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        apiInfo.setPassword(encPassword);
        apiInfo.setRole("ROLE_USER");		        // 기본 역할 'ROLE_USER'
        apiInfo.setNickname(joinForm.getNickname());
        apiInfo.setGender(joinForm.getGender());
        apiInfo.setBirthDate(joinForm.getBirthDate());
        apiInfo.setWithdrawalRequest(false);        // 탈퇴요청 Default - false

        userRepository.save(apiInfo);

        return "";
    }

    // 이메일 매개변수를 사용하는 메서드의 이름을 변경합니다.
    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("User NOT FOUND");
        }
    }

    // 유저네임 매개변수를 사용하는 메서드는 그대로 둡니다.
    @Override
    public User getUser(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new DataNotFoundException("User NOT FOUND");
        }
    }


    @Override
    public String nicknameDuplicateCheck(String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);

        if(optionalUser.isPresent()){
            return "Duplicate";
        } else {
            return "No_Duplicate";
        }
    }

    @Override
    public Page<User> getMemberList(Pageable pageable){
        Page<User> memberList=userRepository.findAll(pageable);
        return memberList;
    }

    @Override
    public User getUserDetail(Integer userId) {
        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

    @Override
    public void AdminUserEdit(AdminUserDTO adminUserDTO, Integer userId) {
        Optional<User> optionalMember = userRepository.findById(userId);
        if (optionalMember.isPresent()) {
            User user = optionalMember.get();
            user.setEmail(adminUserDTO.getEmail());
            user.setPassword(adminUserDTO.getPassword());
            user.setNickname(adminUserDTO.getNickname());
            user.setGender(adminUserDTO.getGender());
            user.setBirthDate(LocalDate.parse(adminUserDTO.getBirthDate()));
            user.setCreateDate(adminUserDTO.getCreateDate());
            user.setRole(adminUserDTO.getRole());
            user.setWithdrawalRequest(adminUserDTO.getWithdrawalRequest());
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Integer userId){
        Optional<User> user=userRepository.findById(userId);
        System.out.println("id="+userId);
        Integer deleteId= user.get().getUserId();
        userRepository.deleteById(deleteId);

    }

    @Override
    public void deleteUsers(List<Integer> userIds) {
        for (Integer userId : userIds) {
            userRepository.deleteById(userId);
        }
    }

    @Override
    public void kakaoUnlink(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void googleUnlink(String accessToken) {
        String URL = "https://oauth2.googleapis.com/revoke";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);
        restTemplate.postForObject(URL, params, String.class);
    }


}
