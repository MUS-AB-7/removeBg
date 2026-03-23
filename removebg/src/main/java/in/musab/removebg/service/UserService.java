package in.musab.removebg.service;

import in.musab.removebg.dto.UserDto;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    UserDto getUserByClerkId(String clerkId);

    void deleteUserIdByClerk(String clerkId);
}
