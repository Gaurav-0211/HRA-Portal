package com.hra.hra.service;

import com.hra.hra.dto.Response;
import jakarta.servlet.http.HttpServletResponse;

public interface SecurityService {

    Response authLogin(String email, String password, HttpServletResponse httpResponse);

}
