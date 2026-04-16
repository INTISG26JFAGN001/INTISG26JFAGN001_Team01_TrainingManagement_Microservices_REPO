package com.cognizant.tms.auth.manager.controller;

import com.cognizant.tms.auth.manager.dto.*;
import com.cognizant.tms.auth.manager.exception.RoleNotFoundException;
import com.cognizant.tms.auth.manager.exception.TokenInvalidException;
import com.cognizant.tms.auth.manager.exception.UserNotFoundException;
import com.cognizant.tms.auth.manager.model.Roles;
import com.cognizant.tms.auth.manager.model.Users;
import com.cognizant.tms.auth.manager.service.CustomUserDetailsService;
import com.cognizant.tms.auth.manager.service.IRolesService;
import com.cognizant.tms.auth.manager.service.IUsersService;
import com.cognizant.tms.auth.manager.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
@Tag(name="Authentication Controller", description="This controller handles all operations related to user " +
        "authentication, user creation and token management. It provides endpoints for user registration, login," +
        " and token refresh. The controller interacts with the user and role services to manage user data and " +
        "roles, and utilizes JWT for secure authentication and authorization.")
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


    /*
        This is my Signup Endpoint.
    */
    @Operation(summary = "Sign Up User Endpoint (ADMIN ONLY)",
    description="This endpoint allows for the registration of new users. It accepts a JSON payload containing the " +
            "user's username, email, full name, password, and roles. The password is securely hashed before being"+
            "stored. Only users with the ADMIN role can access this endpoint to create new users. The endpoint"+
            " also checks if the specified username, email are already existing in the database or not and whether " +
            "the specified roles are valid or not. If the registration is successful, it returns a success message " )
    @ApiResponses(value={
            @ApiResponse(responseCode = "201",
                    content=@Content(mediaType="application/json",
                        schema=@Schema(implementation= SignupResponseDTO.class),
                        examples={
                        @ExampleObject(
                                name="Successful Sign Up",
                                value="{\n" +
                                        "  \"timestamp\": \"2024-06-01T12:00:00\",\n" +
                                        "  \"signUpSuccess\": true,\n" +
                                        "  \"message\": \"Sign up successful. Username: newuser\"\n" +
                                        "}"
                        )
                        }
                    )
            ),
            @ApiResponse(responseCode="403",
                    content=@Content(mediaType="application/json",
                        schema=@Schema(implementation=SignupResponseDTO.class),
                        examples={
                            @ExampleObject(
                                    name="Unauthorized",
                                    value=""
                            )
                        }
                    )
            ),
            @ApiResponse(responseCode="404",
                    content=@Content(mediaType="application/json",
                        schema=@Schema(implementation=SignupResponseDTO.class),
                            examples={
                                    @ExampleObject(
                                            name="Username already Exists",
                                            value="{\n" +
                                                    "    \"timestamp\": \"2026-04-16T14:00:21.0563989\",\n" +
                                                    "    \"message\": \"User with username: associate1 already exists\",\n" +
                                                    "    \"errorCode\": \"U002\",\n" +
                                                    "    \"path\": \"/auth/signup\"\n" +
                                                    "}"
                                    ), @ExampleObject(
                                        name="Email Already Exists",
                                        value="{\n" +
                                                "    \"timestamp\": \"2026-04-16T14:03:29.6134144\",\n" +
                                                "    \"message\": \"User with email: associate1@example.com already exists\",\n" +
                                                "    \"errorCode\": \"U003\",\n" +
                                                "    \"path\": \"/auth/signup\"\n" +
                                                "}"
                                    ), @ExampleObject(
                                                name="Role Does not Exist",
                                                value="{\n" +
                                                        "    \"timestamp\": \"2026-04-16T14:05:59.9112564\",\n" +
                                                        "    \"message\": \"Role ROLE_TEST not found\",\n" +
                                                        "    \"errorCode\": \"R001\",\n" +
                                                        "    \"path\": \"/auth/signup\"\n" +
                                                        "}"
                                    )
                            }
                    )
            )
    })
    @PostMapping("/signup")
    @Transactional
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


    /*
        This is my Login Endpoint.
    */
    @Operation(summary="Login User Endpoint (ALL USERS)", description="This endpoint allows users to log in by " +
            "providing their username and password. It accepts a JSON payload containing the username and password. " +
            "If the credentials are valid, it generates a JWT access token and a refresh token. The access token is " +
            "returned in the response body, while the refresh token is set as an HTTP-only cookie. The endpoint also" +
            " handles various error scenarios such as invalid credentials, user not found, and token generation " +
            "issues.")
    @ApiResponses(value={
            @ApiResponse(responseCode="200",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation= LoginResponseDTO.class),
                            examples={
                            @ExampleObject(
                                    name="Login Successful",
                                    value="{\n" +
                                            "    \"timestamp\": \"2026-04-16T14:42:23.8024162\",\n" +
                                            "    \"message\": \"Welcome Associate CTS\",\n" +
                                            "    \"loginSuccess\": true,\n" +
                                            "    \"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJjbGFpbXMiOlsiUk9MRV9BU1NPQ0lBVEUiXSwic3ViIjoiYXNzb2NpYXRlMSIsImlhdCI6MTc3NjMzMDc0NCwiZXhwIjoxNzc2NDE3MTQ0fQ.Pj2BVSpc4Xtt3FB7tLZ3nIiKhVJjbtCbB4SAwlJALAo\"\n" +
                                            "}"
                            )

                            }
                    )
            ),
            @ApiResponse(responseCode="404",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation= ErrorResponseDTO.class),
                            examples={
                            @ExampleObject(
                                    name="Username Invalid",
                                    value="{\n" +
                                            "    \"timestamp\": \"2026-04-16T14:44:24.9926052\",\n" +
                                            "    \"message\": \"User with username: associate10 does not exist\",\n" +
                                            "    \"errorCode\": \"U002\",\n" +
                                            "    \"path\": \"/auth/login\"\n" +
                                            "}"
                            ), @ExampleObject(
                                    name="Password Invalid",
                                    value="{\n" +
                                            "    \"timestamp\": \"2026-04-16T14:48:58.680244\",\n" +
                                            "    \"message\": \"Invalid password\",\n" +
                                            "    \"errorCode\": \"U004\",\n" +
                                            "    \"path\": \"/auth/login\"\n" +
                                            "}"
                            )
                            }
                    )
            )
    })
    @PostMapping("/login")
    @Transactional
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
                        .httpOnly(true)
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
        catch(BadCredentialsException be){
            throw be;
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


    /*
        This is my Refresh Token Endpoint.
    */
    @Operation(summary="Refresh Token Endpoint (ALL USERS)", description="This endpoint allows users to refresh" +
            " their JWT access token using a valid refresh token. The refresh token is expected to be sent as an " +
            "HTTP-only cookie named refreshToken. If the refresh token is valid, a new access token is generated " +
            "and returned in the response body. The endpoint also handles various error scenarios such as missing " +
            "or invalid refresh tokens.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "202",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation= LoginResponseDTO.class),
                            examples={
                            @ExampleObject(
                                    name="Token Refresh Successful",
                                    value="{\n" +
                                            "    \"timestamp\": \"2026-04-16T14:57:22.5266765\",\n" +
                                            "    \"message\": \"Access Permitted\",\n" +
                                            "    \"loginSuccess\": true,\n" +
                                            "    \"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJjbGFpbXMiOlsiUk9MRV9BU1NPQ0lBVEUiXSwic3ViIjoiYXNzb2NpYXRlMSIsImlhdCI6MTc3NjMzMTY0MiwiZXhwIjoxNzc2NDE4MDQyfQ.V4pwjxxQOPQD5c_zNMnc2CW053i3DfrDdEJXAL963n8\"\n" +
                                            "}"
                            )

                            }
                    )
            ),
            @ApiResponse(responseCode="404",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation= ErrorResponseDTO.class),
                            examples={
                            @ExampleObject(
                                    name="No Token Present",
                                    value="{\n" +
                                            "    \"timestamp\": \"2026-04-16T14:59:24.9926052\",\n" +
                                            "    \"message\": \"No token present\",\n" +
                                            "    \"errorCode\": \"T001\",\n" +
                                            "    \"path\": \"/auth/refresh-token\"\n" +
                                            "}"
                            ), @ExampleObject(
                                    name="Token Invalid",
                                    value="{\n" +
                                            "    \"timestamp\": \"2026-04-16T15:01:58.680244\",\n" +
                                            "    \"message\": \"Token is not Valid\",\n" +
                                            "    \"errorCode\": \"T001\",\n" +
                                            "    \"path\": \"/auth/refresh-token\"\n" +
                                            "}"
                            )
                            }
                    )
            )
    })
    @PostMapping("/refresh-token")
    @Transactional
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){
        String refreshToken=null;
        try {
            refreshToken = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("refreshToken"))
                    .findFirst()
                    .map(c -> c.getValue())
                    .orElse(null);
        }catch(NullPointerException ne){
            throw new TokenInvalidException("No token present");
        }
        catch(Exception e){
            throw new TokenInvalidException(e.getMessage());
        }
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
            System.out.println("No token present"+refreshToken);
            throw new TokenInvalidException("No token present");
        }
        responseDTO.setLoginSuccess(success);
        responseDTO.setAccessToken(token);
        responseDTO.setMessage(message);
        return ResponseEntity.status(status).body(responseDTO);
    }
}
