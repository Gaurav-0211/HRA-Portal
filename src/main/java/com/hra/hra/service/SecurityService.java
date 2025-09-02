package com.hra.hra.service;

import com.hra.hra.dto.Response;

public interface SecurityService {

    Response authLogin(String email, String password, String role);

}
