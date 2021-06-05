package kg.megacom.Authorization;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private List<User> userList;
    @Autowired
    private Long userId;

    public UserController() {
    }

    @GetMapping({"/index"})
    public String showUserList(Model model) {
        model.addAttribute("users", this.userList);
        return "index";
    }

    @GetMapping({"/signup"})
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @PostMapping({"/adduser"})
    public String addUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "add-user";
        } else {
            System.out.println("New user "+user);
            user.setId(this.userId++);
            user.setActive(true);
            System.out.println(user);
            this.userList.add(user);
            return "redirect:/index";
        }

    }

    @GetMapping({"/edit/{id}"})
    public String showUpdateForm(@PathVariable("id") long id, @Valid Model model) {
        User user = (User)this.userList.stream().filter((u) -> { return u.getId() == id; }).findFirst().get();
        model.addAttribute("user", user);
        return "update-user";

//        User user = null;
//        for(User item: userList){
//           if(item.getId() == id){
//               user = item;
//           } else {
//               System.out.println("HAAHHAHAAH");
//           }
//        }
//        System.out.println("Edit "+user);
//
//        model.addAttribute("user", user);
//        return "update-user";
        
    }

    @PostMapping({"/update/{id}"})
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-user";
        } else {
            User user1 = (User)this.userList.stream().filter((u) -> {
                return u.getId() == id;
            }).findFirst().get();
            this.userList.remove(user1);
            this.userList.add(user);
//            System.out.println("Update "+user);
            return "redirect:/index";
        }
    }

    @GetMapping({"/delete/{id}"})
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = (User)this.userList.stream().filter((u) -> {
            return u.getId() == id;
        }).findAny().get();
        this.userList.remove(user);
        return "redirect:/index";
    }
}