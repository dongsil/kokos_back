package com.base.service;

//import com.base.Utils;
import com.base.model.User;
import com.base.dto.UserDTO;
//import com.base.dto.UserParams;
import com.base.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityContextService securityContextService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           SecurityContextService securityContextService) {
        this.userRepository = userRepository;
        this.securityContextService = securityContextService;
    }

    @Override
    public Optional<UserDTO> findOne(Long id) {
        return userRepository.findOneById(id).map(r -> {
            //final Optional<User> currentUser = securityContextService.currentUser();

            // Set email only if it equals with currentUser.
            //final String email = currentUser
            //        .filter(u -> u.equals(r.getUser()))
            //        .map(User::getUsername)
            //        .orElse(null);

            return UserDTO.builder()
                    .id(r.getId())
                    //.email(email)
                    //.avatarHash(Utils.md5(r.getUser().getUsername()))
                    .name(r.getName())
                    .build();
        });
    }

//    @Override
//    public Optional<UserDTO> findMe() {
//        return securityContextService.currentUser().flatMap(u -> findOne(u.getId()));
//    }
//
//    @Override
//    public Page<UserDTO> findAll(PageRequest pageable) {
//        return userRepository.findAll(pageable).map(u -> UserDTO.builder()
//                .id(u.getId())
//                .name(u.getName())
//                .avatarHash(Utils.md5(u.getUsername()))
//                .build()
//        );
//    }
//
//    @Override
//    public User create(UserParams params) {
//        return userRepository.save(params.toUser());
//    }
//
//    @Override
//    public User update(User user, UserParams params) {
//        params.getEmail().ifPresent(user::setUsername);
//        params.getEncodedPassword().ifPresent(user::setPassword);
//        params.getName().ifPresent(user::setName);
//        return userRepository.save(user);
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println("loadUserByUsername username : " + username);
        final Optional<User> user = userRepository.findOneByUsername(username);
    	System.out.println("loadUserByUsername user : " + user.toString());        
        final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
        user.ifPresent(detailsChecker::check);
        return user.orElseThrow(() -> new UsernameNotFoundException("user not found."));
    }

}
