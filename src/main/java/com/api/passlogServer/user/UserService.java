package com.api.passlogServer.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service @Slf4j @RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    boolean userExists;

//    @Autowired
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<Users> optionalUsers = userRepository.findUsersByUsername(username);
        if(optionalUsers.isEmpty()){
            log.error("User with username: "+username+" does not exist!");
            throw new UsernameNotFoundException("Username does not exist!");
        }else
        {
            log.info("User found as: "+optionalUsers.get().getUsername());
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            optionalUsers.get().getRoles().forEach(role ->{
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new User(optionalUsers.get().getUsername(),optionalUsers.get().getPassword(),authorities);
        }
    }

    public boolean addUser(Users users) {
        userExists = userRepository.existsByUsername(users.getUsername());
        if(userExists){
            log.warn("User with username: "+users.getUsername()+" already exists");
        }else {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            log.info("Saving to database: "+users.toString());
            userRepository.save(users);
        }
        return userExists;
    }

    public void addRole(Role role){
        roleRepository.save(role);
    }

    @Transactional
    public void addRoleToUser(String username, String roleName){
        Optional<Users> optionalUser = userRepository.findUsersByUsername(username);
        if(optionalUser.isPresent()){
            Users user = optionalUser.get();
            Role role = roleRepository.findByName(roleName);
            user.getRoles().add(role);
        }else{
            log.error("User with username "+username+" could not be found");
            throw new UsernameNotFoundException("User with username ["+username+"] could not be found");
        }
    }

    public Users getUserLogin(String token) {
        Algorithm algorithm = Algorithm.HMAC256("sekret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        String authorizationToken = token.substring("Bearer ".length());
        DecodedJWT decodedJWT = verifier.verify(authorizationToken);
        String username = decodedJWT.getSubject();
        log.info("Searching for user with username: "+username);
        Optional<Users> optionalUser = userRepository.findByUsername(username);
        Users userReturned = optionalUser.get();
        if(optionalUser.isEmpty()){
            log.warn("User with username: "+userReturned.getUsername()+" does not exist");
            throw new IllegalStateException("User does not exist!");
        }else
            log.info("Returning "+userReturned.toString());
            return new Users(userReturned.getId(),userReturned.getFname(),userReturned.getLname(),
                    userReturned.getUsername(),userReturned.getTheme());
    }

    @Transactional
    public void setUserTheme(Users users) {
        Optional<Users> optionalUsers = userRepository.findById(users.getId());
        if(optionalUsers.isEmpty()){
            log.info("User with id: "+users.getId()+" does not exist");
            throw new IllegalStateException("User does not exist!");
        }else
        {
            log.info("Saving user's application theme as: "+ users.getTheme());
            optionalUsers.get().setTheme(users.getTheme());
        }
    }

    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("sekret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Users users = userRepository.findUsersByUsername(username).get();
                log.info("Creating new access token for user: "+username);
                String accessToken = JWT.create()
                        .withSubject(users.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",users.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                String[] tokens = {accessToken,refreshToken};
                response.setContentType(APPLICATION_JSON_VALUE);
                log.info("Sending JWT tokens to user: "+username);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception exception){
                log.error("refresh_token: "+exception.getMessage());
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //   response.sendError(FORBIDDEN.value());
                HashMap<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                log.info("Sending error message as HTTP response");
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
