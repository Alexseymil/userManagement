package com.management.conroller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.management.model.UserAccount;
import com.management.repositories.UserCrudRepository;

@Controller
public class UserController {

	@Autowired
	private UserCrudRepository userCrudRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@GetMapping("/user")
	public String allUser(Model model) {

		model.addAttribute("allUsers", userCrudRepository.findAll());

		return "List";
	}

	@GetMapping("/user/{id}")
	public String infoUser(@PathVariable(value = "id") Long id, Model model) {
		UserAccount user = userCrudRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("user", user);

		return "View";
	}

	@GetMapping("/new")
	public String addUser(UserAccount userAccount) {

		return "New";
	}

	@PostMapping("/adduser")
	public String addUser(@Valid UserAccount userAccount, BindingResult result, Model model,
			HttpServletRequest request) {
		if (result.hasErrors()) {
			return "New";
		}

		String encryptedPassword = encoder.encode(userAccount.getUserPassword());
		userAccount.setUserPassword(encryptedPassword);
		if (request.getParameter("Lock") == null) {
			userAccount.setStatus(true);
		}
		userCrudRepository.save(userAccount);
		model.addAttribute("allUsers", userCrudRepository.findAll());
		return "List";
	}

	@GetMapping("/403")
	public String error403() {
		return "/error/403";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		UserAccount user = userCrudRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userCrudRepository.delete(user);
		model.addAttribute("allUsers", userCrudRepository.findAll());
		return "List";
	}

	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable("id") long id, Model model) {
		UserAccount user = userCrudRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		model.addAttribute("userAccount", user);
		return "Edit";
	}

	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") long id, @Valid UserAccount user, BindingResult result, Model model,
			HttpServletRequest request) {
		if (result.hasErrors()) {
			user.setUserId(id);
			return "Edit";
		}
		String encryptedPassword = encoder.encode(user.getUserPassword());
		user.setUserPassword(encryptedPassword);
		if (request.getParameter("Lock") == null) {
			user.setStatus(true);
		}
		user.setUserId(id);
		userCrudRepository.save(user);
		model.addAttribute("allUsers", userCrudRepository.findAll());
		return "List";
	}

}
