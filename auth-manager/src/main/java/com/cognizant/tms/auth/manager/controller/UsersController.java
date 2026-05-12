package com.cognizant.tms.auth.manager.controller;

import com.cognizant.tms.auth.manager.dto.ErrorResponseDTO;
import com.cognizant.tms.auth.manager.dto.UsersDTO;
import com.cognizant.tms.auth.manager.mapper.UserMapper;
import com.cognizant.tms.auth.manager.model.Users;
import com.cognizant.tms.auth.manager.service.IUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name="Users Controller", description = "This controller handles all user related operations like fetching user " +
        "details, updating user information and deleting user")
public class UsersController {
    private IUsersService usersService;
    private UserMapper userMapper;

    @Autowired
    public UsersController(IUsersService usersService, UserMapper userMapper) {
        this.usersService = usersService;
        this.userMapper = userMapper;
    }

    @Operation(summary="Get all users (ADMIN ONLY)", description = "Fetches the details of all users present in the system")
    @ApiResponses(value={
            @ApiResponse(responseCode="200",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = UsersDTO.class),
                            examples={
                            @ExampleObject(
                                    name="Success Response",
                                    value="[\n" +
                                            "  {\n" +
                                            "    \"id\": 1,\n" +
                                            "    \"username\": \"admin@cts.com\",\n" +
                                            "    \"fullName\": \"ADMIN CTS\",\n" +
                                            "    \"email\": \"admin@cts.com\",\n" +
                                            "    \"roles\": [\n" +
                                            "      \"ROLE_ADMIN\"\n" +
                                            "    ]\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"id\": 2,\n" +
                                            "    \"username\": \"user@example.com\",\n" +
                                            "    \"fullName\": \"string\",\n" +
                                            "    \"email\": \"user@example.com\",\n" +
                                            "    \"roles\": [\n" +
                                            "      \"ROLE_ADMIN\"\n" +
                                            "    ]\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"id\": 3,\n" +
                                            "    \"username\": \"trainer@example.com\",\n" +
                                            "    \"fullName\": \"Trainer CTS\",\n" +
                                            "    \"email\": \"trainer@example.com\",\n" +
                                            "    \"roles\": [\n" +
                                            "      \"ROLE_TRAINER\"\n" +
                                            "    ]\n" +
                                            "  },\n" +
                                            "]"
                            )
                            }
                    )
            ),
            @ApiResponse(responseCode="403",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation=ErrorResponseDTO.class),
                            examples={
                                    @ExampleObject(
                                            name="Forbidden Response",
                                            value=""
                                    )
                            }
                    )
            )
    })
    @GetMapping("/all")
    public ResponseEntity<List<UsersDTO>> getAllUsers(){
        List<Users> usersList = usersService.getAllUsers();
        List<UsersDTO> usersDTOS = usersList.stream().map(u-> userMapper.mapToUsersDTO(u)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(usersDTOS);
    }


    @Operation(summary = "Get user by id", description = "Fetches the details of a user based on the provided id")
    @ApiResponses(value={
            @ApiResponse(responseCode="200",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = UsersDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"username\": \"admin@cts.com\",\n" +
                                                    "  \"fullName\": \"ADMIN CTS\",\n" +
                                                    "  \"email\": \"admin@cts.com\",\n" +
                                                    "  \"roles\": [\n" +
                                                    "    \"ROLE_ADMIN\"\n" +
                                                    "  ]\n" +
                                                    "}"
                                    )
                            }
                    )
            ),  @ApiResponse(responseCode="403",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = ErrorResponseDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = ""
                                    )
                            }
                    )
            ),  @ApiResponse(responseCode="404",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = ErrorResponseDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "User ID Does not Exist",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T15:22:58.9825949\",\n" +
                                                    "  \"message\": \"User with id: 100 does not exist\",\n" +
                                                    "  \"errorCode\": \"U001\",\n" +
                                                    "  \"path\": \"/user/100\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsersDTO> getById(@PathVariable long id){
        Users users = usersService.getUserById(id);
        UsersDTO usersDTO = userMapper.mapToUsersDTO(users);
        return ResponseEntity.status(HttpStatus.OK).body(usersDTO);
    }


    @Operation(summary = "Get user by username", description = "Fetches the details of a user based on the provided username")
    @ApiResponses(value={
            @ApiResponse(responseCode="200",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = UsersDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"username\": \"admin@cts.com\",\n" +
                                                    "  \"fullName\": \"ADMIN CTS\",\n" +
                                                    "  \"email\": \"admin@cts.com\",\n" +
                                                    "  \"roles\": [\n" +
                                                    "    \"ROLE_ADMIN\"\n" +
                                                    "  ]\n" +
                                                    "}"
                                    )
                            }
                    )
            ), @ApiResponse(responseCode="403",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = ErrorResponseDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = ""
                                    )
                            }
                    )
            ), @ApiResponse(responseCode="404",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = ErrorResponseDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = ""
                                    )
                            }
                    )
            )
    })
    @GetMapping("/username")
    public ResponseEntity<UsersDTO> getByUsername(@RequestParam("key") String username){
        Users users = usersService.getUserByUsername(username);
        UsersDTO usersDTO = userMapper.mapToUsersDTO(users);
        return ResponseEntity.status(HttpStatus.OK).body(usersDTO);
    }

    @Operation(summary = "Get user by email", description = "Fetches the details of a user based on the provided email")
    @ApiResponses(value={
            @ApiResponse(responseCode="200",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = UsersDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = "{\n" +
                                                    "  \"id\": 1,\n" +
                                                    "  \"username\": \"admin@cts.com\",\n" +
                                                    "  \"fullName\": \"ADMIN CTS\",\n" +
                                                    "  \"email\": \"admin@cts.com\",\n" +
                                                    "  \"roles\": [\n" +
                                                    "    \"ROLE_ADMIN\"\n" +
                                                    "  ]\n" +
                                                    "}"
                                    )
                            }
                    )
            ), @ApiResponse(responseCode="404",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = ErrorResponseDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Email ID Does not Exist",
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2026-04-16T15:09:44.1456973\",\n" +
                                                    "  \"message\": \"User with email: admin@cts.in does not exist\",\n" +
                                                    "  \"errorCode\": \"U003\",\n" +
                                                    "  \"path\": \"/user/email\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ), @ApiResponse(responseCode="403",
                    content=@Content(mediaType="application/json",
                    schema=@Schema(implementation = ErrorResponseDTO.class),
                    examples= {
                            @ExampleObject(
                                    name = "Forbidden Response",
                                    value = ""
                            )
                    }
            )
    )
    })
    @GetMapping("/email")
    public ResponseEntity<UsersDTO> getByEmail(@RequestParam("key") String email){
        Users users = usersService.getUserByEmail(email);
        UsersDTO usersDTO = userMapper.mapToUsersDTO(users);
        return ResponseEntity.status(HttpStatus.OK).body(usersDTO);
    }


    @Operation(summary = "Get users by full name", description = "Fetches the details of users based on" +
            " the provided full name. It returns a list of users whose full name contains the provided key")
    @ApiResponses(value={
            @ApiResponse(responseCode="200",
                    content =@Content(mediaType="application/json",
                        schema=@Schema(implementation=UsersDTO.class),
                        examples={
                                @ExampleObject(
                                        name="Success Response",
                                        value="[\n" +
                                                "  {\n" +
                                                "    \"id\": 4,\n" +
                                                "    \"username\": \"associate@example.com\",\n" +
                                                "    \"fullName\": \"Associate CTS\",\n" +
                                                "    \"email\": \"associate@example.com\",\n" +
                                                "    \"roles\": [\n" +
                                                "      \"ROLE_ASSOCIATE\"\n" +
                                                "    ]\n" +
                                                "  },\n" +
                                                "  {\n" +
                                                "    \"id\": 5,\n" +
                                                "    \"username\": \"associate1@example.com\",\n" +
                                                "    \"fullName\": \"Associate CTS\",\n" +
                                                "    \"email\": \"associate1@example.com\",\n" +
                                                "    \"roles\": [\n" +
                                                "      \"ROLE_ASSOCIATE\"\n" +
                                                "    ]\n" +
                                                "  }\n" +
                                                "]"
                                ),@ExampleObject(
                                name="Success Response with no results",
                                value="[]"
                        )
                        }
                    )
            ), @ApiResponse(responseCode="403",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = ErrorResponseDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = ""
                                    )
                            }
                    )
            )

    }
    )
    @GetMapping("/name")
    public ResponseEntity<List<UsersDTO>> getUsersByName(@RequestParam("key") String fullname){
        List<Users> usersList = usersService.getUsersByFullName(fullname);
        List<UsersDTO> usersDTOS = usersList.stream().map(u-> userMapper.mapToUsersDTO(u)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(usersDTOS);
    }

    @Operation(summary = "Update user details (ADMIN ONLY)", description = "Updates the information of an existing user based on the provided details. " +
            "The user is identified by the id field in the request body. All other fields are updated with the provided values")
    @ApiResponses(value={
            @ApiResponse(responseCode = "202",
                    content=@Content(mediaType="application/json",
                        schema=@Schema(implementation=UsersDTO.class),
                            examples={
                            @ExampleObject(
                                    name="Success Response",
                                    value="{\n" +
                                            "  \"id\": 2,\n" +
                                            "  \"username\": \"trainer@cts.com\",\n" +
                                            "  \"fullName\": \"string\",\n" +
                                            "  \"email\": \"trainer@cts.com\",\n" +
                                            "  \"roles\": [\n" +
                                            "    \"ROLE_TRAINER\"\n" +
                                            "  ]\n" +
                                            "}"
                            )
                            }
                    )
            ),
            @ApiResponse(responseCode="404",
                content=@Content(mediaType="application/json",
                    schema=@Schema(implementation=ErrorResponseDTO.class),
                    examples={
                            @ExampleObject(
                                    name="Role Does not Exist",
                                    value="{\n" +
                                            "  \"timestamp\": \"2026-04-16T17:51:18.6693788\",\n" +
                                            "  \"message\": \"Role not found: ROLE_ROLE\",\n" +
                                            "  \"errorCode\": \"R001\",\n" +
                                            "  \"path\": \"/user/update\"\n" +
                                            "}"
                            ), @ExampleObject(
                                    name="User ID Does not Exist",
                                    value="{\n" +
                                            "  \"timestamp\": \"2026-04-16T18:00:57.5407868\",\n" +
                                            "  \"message\": \"User with id: 20 does not exist\",\n" +
                                            "  \"errorCode\": \"U001\",\n" +
                                            "  \"path\": \"/user/update\"\n" +
                                            "}"
                            ), @ExampleObject(
                                    name="Username taken by another user",
                                    value="{\n" +
                                            "  \"timestamp\": \"2026-04-16T18:01:45.6821877\",\n" +
                                            "  \"message\": \"User with username: trainer already exists\",\n" +
                                            "  \"errorCode\": \"U002\",\n" +
                                            "  \"path\": \"/user/update\"\n" +
                                            "}"
                            ), @ExampleObject(
                                    name="Email ID taken by another user",
                                    value="{\n" +
                                            "  \"timestamp\": \"2026-04-16T18:05:41.6039352\",\n" +
                                            "  \"message\": \"User with email: trainer@example.com already exists\",\n" +
                                            "  \"errorCode\": \"U003\",\n" +
                                            "  \"path\": \"/user/update\"\n" +
                                            "}"
                            )
                    }
                )
            ), @ApiResponse(responseCode="403",
                    content=@Content(mediaType="application/json",
                            schema=@Schema(implementation = ErrorResponseDTO.class),
                            examples= {
                                    @ExampleObject(
                                            name = "Forbidden Response",
                                            value = ""
                                    )
                            }
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<UsersDTO> updateUser(@RequestBody UsersDTO usersDTO){
        Users users = userMapper.mapToUsers(usersDTO);
        Users updatedUser = usersService.updateUser(users);
        UsersDTO updatedUserDTO = userMapper.mapToUsersDTO(updatedUser);
        System.out.println(updatedUserDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUserDTO);
    }

    @Operation(summary = "Delete user by id (ADMIN ONLY)", description = "Deletes an existing user from the system " +
            "based on the provided id. It returns the details of the deleted user in the response body")
    @ApiResponses(value={
            @ApiResponse(responseCode="200",
                content=@Content(mediaType="application/json",
                    schema=@Schema(implementation=UsersDTO.class),
                    examples={
                            @ExampleObject(
                                    name="Success Response",
                                    value="{\n" +
                                            "  \"id\": 2,\n" +
                                            "  \"username\": \"trainer@cts.com\",\n" +
                                            "  \"fullName\": \"string\",\n" +
                                            "  \"email\": \"trainer@cts.com\",\n" +
                                            "  \"roles\": [\n" +
                                            "    \"ROLE_TRAINER\"\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    }
                )
            ), @ApiResponse(responseCode="404",
                    content=@Content(mediaType="application/json",
                        schema=@Schema(implementation=ErrorResponseDTO.class),
                            examples={
                            @ExampleObject(
                                    name="User ID Does not Exist",
                                    value="{\n" +
                                            "  \"timestamp\": \"2026-04-16T18:09:34.1881366\",\n" +
                                            "  \"message\": \"User with id: 20 does not exist\",\n" +
                                            "  \"errorCode\": \"U001\",\n" +
                                            "  \"path\": \"/user/20\"\n" +
                                            "}"
                            )
                            }
                    )
            ), @ApiResponse(responseCode="403",
            content=@Content(mediaType="application/json",
                    schema=@Schema(implementation = ErrorResponseDTO.class),
                    examples= {
                            @ExampleObject(
                                    name = "Forbidden Response",
                                    value = ""
                            )
                    }
            )
    )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UsersDTO> deleteUser(@PathVariable long id){
        Users users = usersService.deleteUserById(id);
        UsersDTO usersDTO = userMapper.mapToUsersDTO(users);
        return ResponseEntity.status(HttpStatus.OK).body(usersDTO);
    }
}
