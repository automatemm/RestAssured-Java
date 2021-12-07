package com.hsbc.utils;

public enum Routes {
  GET_USERS("/users"),
  CREATE_USER("/users");

  private final String endpoint;

  Routes(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getEndpoint() {
    return endpoint;
  }
}
