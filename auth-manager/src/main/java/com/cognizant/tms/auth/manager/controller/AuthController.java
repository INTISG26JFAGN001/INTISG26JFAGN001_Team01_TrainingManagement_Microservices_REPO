package com.cognizant.tms.auth.manager.controller;

import com.cognizant.tms.auth.manager.dto.LoginRequestDTO;
import com.cognizant.tms.auth.manager.dto.LoginResponseDTO;
import com.cognizant.tms.auth.manager.dto.SignupRequestDTO;
import com.cognizant.tms.auth.manager.dto.SignupResponseDTO;
import com.cognizant.tms.auth.manager.exception.RoleNotFoundException;
import com.cognizant.tms.auth.manager.exception.TokenInvalidException;
import com.cognizant.tms.auth.manager.exception.UserNotFoundException;
import com.cognizant.tms.auth.manager.model.Roles;
import com.cognizant.tms.auth.manager.model.Users;
import com.cognizant.tms.auth.manager.service.CustomUserDetailsService;
import com.cognizant.tms.auth.manager.service.IRolesService;
import com.cognizant.tms.auth.manager.service.IUsersService;
import com.cognizant.tms.auth.manager.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IUsersService usersService;
    private final IRolesService rolesService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthController(IUsersService usersService, IRolesService rolesService, AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder, AuthUtil authUtil, CustomUserDetailsService userDetailsService){
        this.usersService = usersService;
        this.rolesService = rolesService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.authUtil = authUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@Valid @RequestBody SignupRequestDTO requestDTO) throws Exception{
        SignupResponseDTO responseDTO = new SignupResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        boolean success = false;
        String message = "Login Failed";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        try{
            Users user = new Users();
            user.setUsername(requestDTO.getUsername());
            user.setEmail(requestDTO.getEmail());
            user.setFullName(requestDTO.getFullName());
            user.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));
            List<String> roles = requestDTO.getRoles();
            List<Roles> rolesList = roles.stream().map(role->{
                if(!role.startsWith("ROLE_")){
                    role = "ROLE_"+role;
                }
                Roles r = rolesService.getByRoleName(role);
                if(r==null){
                    throw new RoleNotFoundException("Role "+role+" not found");
                }
                return r;
            }).toList();
            user.setRoles(rolesList);
            usersService.createUser(user);
            message="Sign up successful. Username: "+requestDTO.getUsername();
            success = true;
            status = HttpStatus.CREATED;
        }
        catch(UserNotFoundException | RoleNotFoundException | TokenInvalidException ue){
            throw ue;
        }
        catch(Exception e){
            System.out.println(e);
            throw new Exception("Sign up Failed");
        }
        responseDTO.setMessage(message);
        responseDTO.setSignUpSuccess(success);
        return ResponseEntity.status(status).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO, HttpServletResponse response) throws Exception{
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        boolean success = false;
        String message = "Login Failed";
        String token = null;
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        try{
            Users user = usersService.getUserByUsername(requestDTO.getUsername());
            if(user!=null){
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDTO.getUsername(),
                                requestDTO.getPassword()
                        )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetails userDetails = userDetailsService.loadUserByUsername(requestDTO.getUsername());
                String accessToken = authUtil.generateAccessToken(userDetails);
                String refreshToken = authUtil.generateRefreshToken(userDetails);
                ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                        .httpOnly(false)
                        .secure(false)
                        .path("/auth/refresh-token")
                        .maxAge(7*24*60*60)
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                token = accessToken;
                message = "Welcome "+user.getFullName();
                success = true;
                status = HttpStatus.ACCEPTED;
            }
        }
        catch(UserNotFoundException ue){
            throw ue;
        }
        catch(TokenInvalidException te){
            throw te;
        }
        catch(Exception e){
            System.out.println(e);
            throw new Exception("Login Failed");
        }
        responseDTO.setMessage(message);
        responseDTO.setLoginSuccess(success);
        responseDTO.setAccessToken(token);
        return ResponseEntity.status(status).body(responseDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c->c.getName().equals("refreshToken"))
                .findFirst()
                .map(c->c.getValue())
                .orElse(null);
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        boolean success = false;
        String message = "Login Failed";
        String token = null;
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        if(refreshToken!=null){
            if(authUtil.validToken(refreshToken)){
                String username = authUtil.parseSubject(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(userDetails==null){
                    throw new UserNotFoundException("S500", "User not found");
                }
                String accessToken = authUtil.generateAccessToken(userDetails);
                token = accessToken;
                message = "Access Permitted";
                success = true;
                status = HttpStatus.ACCEPTED;
            }else{
                throw new TokenInvalidException("Token is not Valid");
            }
        }else{
            throw new TokenInvalidException("No token present");
        }
        responseDTO.setLoginSuccess(success);
        responseDTO.setAccessToken(token);
        responseDTO.setMessage(message);
        return ResponseEntity.status(status).body(responseDTO);
    }
}
