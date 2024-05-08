package com.example.demo.controllers;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
	static Logger log = Logger.getLogger(UserController.class);
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		Optional<User> user=userRepository.findById(id);
		if(!user.isPresent())
            log.error(String.format("Failure : user id does not exist %d.",id));
         else
			log.info(String.format("Success : user id found %d.",id));
		return ResponseEntity.of(user);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null)
			log.error(String.format("Failure : user does not exist %s.",username));
		else
			log.info(String.format("Success : user %s found.",username));
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			//System.out.println("Error - Either length is less than 7 or pass and conf pass do not match. Unable to create ",
			//		createUserRequest.getUsername());
			log.error("Failure : password is either too short or the passwords don't match.");
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);

		log.info(String.format("Success : user %s created.",user.getUsername()));
		return ResponseEntity.ok(user);
	}
	
}
