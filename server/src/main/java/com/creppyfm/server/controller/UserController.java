package com.creppyfm.server.controller;

import com.creppyfm.server.model.Task;
import com.creppyfm.server.model.User;
import com.creppyfm.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
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
@CrossOrigin
@RequestMapping("/api/users")
@Tag(name = "User Controller")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            description = "GET endpoint to fetch all users in 'User' MongoDB collection.",
            summary = "GET All Users"
    )
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(userService.findAllUsers(), HttpStatus.OK);
    }

    @Operation(
            description = "GET endpoint to fetch individual user from 'User' MongoDB collection.\n " +
                    "Accesses user via 'userId'.",
            summary = "GET Individual User",
            parameters = {@Parameter(
                    name = "userId",
                    description = "Path variable 'userId' corresponds to the unique id given to each " +
                            "user upon creation.",
                    example = "6459465f66a8a31dae1c56da"
            )}
    )
    @GetMapping("/{userId}")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        return new ResponseEntity<User>(userService.findUserById(userId), HttpStatus.OK);
    }

    @Operation(
            description = "GET endpoint to fetch all of the specified user's current tasks.\n " +
                    "Accesses user via 'userId'.",
            summary = "GET All Current Tasks",
            parameters = {@Parameter(
                    name = "userId",
                    description = "Path variable 'userId' corresponds to the unique id given to each " +
                            "user upon creation.",
                    example = "6459465f66a8a31dae1c56da"
            )}
    )
    @GetMapping("/{userId}/current-tasks")
    public ResponseEntity<List<Task>> getAllCurrentTasks(@PathVariable String userId) {
        return new ResponseEntity<List<Task>>(userService.findCurrentTasksById(userId), HttpStatus.OK);
    }

    @Operation(
            description = "GET endpoint to fetch all of the specified user's strengths.\n " +
                    "Accesses user via 'userId'.",
            summary = "GET All Strengths",
            parameters = {@Parameter(
                    name = "userId",
                    description = "Path variable 'userId' corresponds to the unique id given to each " +
                            "user upon creation.",
                    example = "6459465f66a8a31dae1c56da"
            )}
    )
    @GetMapping("/{userId}/strengths")
    public ResponseEntity<List<String>> getAllStrengths(@PathVariable String userId) {
        return new ResponseEntity<List<String>>(userService.findStrengthsById(userId), HttpStatus.OK);
    }

    @Operation(
            description = "POST endpoint to create a new user in the 'User' MongoDB collection.",
            summary = "POST Create User",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Path variable 'user' is a RequestBody that takes the necessary " +
                            "fields to create a new user, as defined in the 'User' model."
            )
    )
    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @Operation(
            description = "PUT endpoint to modify the specified user's information.\n " +
                    "Accesses user via 'userId'.",
            summary = "PUT Update User",
            parameters = {@Parameter(
                    name = "userId",
                    description = "Path variable 'userId' corresponds to the unique id given to each " +
                            "user upon creation.",
                    example = "6459465f66a8a31dae1c56da"
            )},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") String userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            description = "DELETE endpoint to delete the specified user from the 'User' collection in MongoDB.\n " +
                    "Accesses user via 'userId'.",
            summary = "DELETE User",
            parameters = {@Parameter(
                    name = "userId",
                    description = "Path variable 'userId' corresponds to the unique id given to each " +
                            "user upon creation.",
                    example = "6459465f66a8a31dae1c56da"
            )},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
