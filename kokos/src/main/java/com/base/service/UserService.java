package com.base.service;

import com.base.model.User;
//import com.base.dto.PageParams;
import com.base.dto.UserDTO;
//import com.base.dto.UserParams;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<UserDTO> findOne(Long id);

//    Optional<UserDTO> findMe();

//    Page<UserDTO> findAll(PageRequest pageable);

//    User create(UserParams params);

//    User update(User user, UserParams params);

//    User updateMe(UserParams params);


}
