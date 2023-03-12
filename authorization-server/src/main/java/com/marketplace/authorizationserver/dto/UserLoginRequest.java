package com.marketplace.authorizationserver.dto;

public record UserLoginRequest(String email, String password) {}