package com.shaka.finte.service;

import com.shaka.finte.DTO.CreateUserRequest;
import com.shaka.finte.DTO.CreateUserResponse;

public interface UserService {

    CreateUserResponse createUser(CreateUserRequest request);
}
