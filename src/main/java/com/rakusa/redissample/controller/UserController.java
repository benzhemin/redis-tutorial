package com.rakusa.redissample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakusa.redissample.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  static class Constants {
    static final String SAMPLE_USER = "sample:user";
  }

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @RequestMapping(value = "/saveCurrentUser", method = RequestMethod.POST)
  public String saveCurrentUser(
      @RequestParam("name") String name,
      @RequestParam("id") String id) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    User user = new User();
    user.setId(id);
    user.setName(name);

    String userJson = mapper.writeValueAsString(user);

    redisTemplate.opsForHash().put(Constants.SAMPLE_USER, id, userJson);

    return userJson;
  }

  @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
  public String getCurrentUser(
      @RequestParam("id") String id) {

    String userJson = (String) redisTemplate.opsForHash().get(Constants.SAMPLE_USER, id);

    return userJson;
  }
}
