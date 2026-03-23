package in.musab.removebg.controller;

import in.musab.removebg.dto.UserDto;
import in.musab.removebg.response.RemoveBgResponse;
import in.musab.removebg.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createOrUpdateUser(@RequestBody UserDto userDto, Authentication authentication) {
        RemoveBgResponse response = null;
        try {
            if (!authentication.getName().equals(userDto.getClerkId())) {
                response = RemoveBgResponse.builder()
                        .success(false)
                        .data("User does not have permission to access the resource")
                        .statusCode(HttpStatus.FORBIDDEN)
                        .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            UserDto user = userService.saveUser(userDto);

            response = RemoveBgResponse.builder()
                    .success(true)
                    .data(user)
                    .statusCode(HttpStatus.OK)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            response = RemoveBgResponse.builder()
                    .success(false)
                    .data(exception.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/credits")
    public ResponseEntity<?> getUserCredits(Authentication authentication){
        RemoveBgResponse bgResponse = null;

        try{
            if(authentication.getName().isEmpty() || authentication.getName() == null){
                bgResponse = RemoveBgResponse.builder()
                .statusCode(HttpStatus.FORBIDDEN)
                .data("User doe not have permission/access to this reaourse")
                .success(false)
                .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(bgResponse);
            }

            String clerkId = authentication.getName();
            UserDto existingUser = userService.getUserByClerkId(clerkId);
            Map<String, Integer> map = new HashMap<>();
            map.put("credits", existingUser.getCredits());

            bgResponse = RemoveBgResponse.builder()
            .statusCode(HttpStatus.OK)
            .data(map)
            .success(true)
            .build();

            return ResponseEntity.status(HttpStatus.OK)
            .body(bgResponse);
        }catch (Exception e){
            bgResponse = RemoveBgResponse.builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
            .data("Something went wrong!")
            .success(false)
            .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(bgResponse);
        }
    }
}
