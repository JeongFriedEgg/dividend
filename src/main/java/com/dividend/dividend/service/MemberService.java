package com.dividend.dividend.service;

import com.dividend.dividend.exception.impl.AlreadyExistUserException;
import com.dividend.dividend.model.Auth;
import com.dividend.dividend.persist.MemberRepository;
import com.dividend.dividend.persist.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberEntity register(Auth.SignUp member) {
        boolean exists = memberRepository.existsByUsername(member.getUsername());

        if (exists) {
            throw new AlreadyExistUserException();
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member.toEntity());
    }
}
