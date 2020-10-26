package com.management.conroller;

import com.management.exception.IdIsCreatedException;
import com.management.model.UserAccount;
import com.management.repositories.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/user")
    public String allUser(Model model) {
        getAllUsers(model);
        return "List";
    }

    @PostMapping("/sort")
    public String allUserSort(Model model, HttpServletRequest request) {
        String role = request.getParameter("role");
        model.addAttribute("allUsers", userCrudRepository.findAllByRole(role));
        return "List";
    }

    @GetMapping("/userName")
    @ResponseBody
    public UserAccount getUserName(@RequestParam(name = "name") String name) {
        return userCrudRepository.findByUserName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User " + name + " was not found in the database"));
    }

    @ResponseBody
    @PostMapping("/addUserN")
    UserAccount save(@RequestBody UserAccount userAccount) {
        userAccount.setUserPassword(encoder.encode(userAccount.getUserPassword()));
        return userCrudRepository.save(userAccount);
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
                          HttpServletRequest request) throws IdIsCreatedException {
        if(userCrudRepository.findById(userAccount.getUserId()).isPresent()){
            throw new IdIsCreatedException("This id is found");
        }
        if (result.hasErrors()) {
            return "New";
        }
        userAccount.setUserPassword(encoder.encode(userAccount.getUserPassword()));
        if (request.getParameter("Lock") == null) {
            userAccount.setStatus(true);
        }
        userCrudRepository.save(userAccount);
        getAllUsers(model);
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
        getAllUsers(model);
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
        user.setUserPassword(encoder.encode(user.getUserPassword()));
        if (request.getParameter("Lock") == null) {
            user.setStatus(true);
        }
        user.setUserId(id);
        userCrudRepository.save(user);
        model.addAttribute("allUsers", userCrudRepository.findAll());
        return "List";
    }

    public void getAllUsers(Model model) {
        model.addAttribute("allUsers", userCrudRepository.findAll());
    }

}
