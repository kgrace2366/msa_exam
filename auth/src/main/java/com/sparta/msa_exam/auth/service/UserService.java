package com.sparta.msa_exam.auth.service;

import com.sparta.msa_exam.auth.dto.SignupRequestDto;
import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.exception.DuplicateResourceException;
import com.sparta.msa_exam.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        password = passwordEncoder.encode(password);

        Optional<User> checkUsername = authRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DuplicateResourceException("중복된 아이디입니다.");
        }

        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        authRepository.save(user);
    }

}
