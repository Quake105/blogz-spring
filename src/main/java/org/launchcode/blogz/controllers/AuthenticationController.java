package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {

		// TODO - implement signup

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");

		boolean isValidUser = org.launchcode.blogz.models.User.isValidUsername(username);

		boolean isValidPassword = org.launchcode.blogz.models.User.isValidUsername(password);

		boolean isVerify = false;
		if(password.equals(verify))
		{
			isVerify = true;

		} else {

			isVerify = false;

		}

		boolean isNotExistingUser = true;
		User existingUser = userDao.findByUsername(username);

		try {
			if (existingUser.getUsername().equals(username))
			{
				isNotExistingUser =  false;
				model.addAttribute("username_error", "Username already exists!");
				return "signup";
			}
		}
		catch(NullPointerException e)
		{
			isNotExistingUser = true;

		}

		if(isValidUser && isValidPassword  && isVerify && isNotExistingUser){


			User userEntry = new User(username, password);
			userDao.save(userEntry);
			HttpSession thisSession = request.getSession();
			setUserInSession(thisSession, userEntry);
			return "redirect:blog/newpost";	
		} 

		if(!isValidUser) 

		{
			model.addAttribute("username_error", "Please enter a valid username!");
			return "signup";
		}

		if(!isValidPassword) 

		{
			model.addAttribute("username", username);
			model.addAttribute("password_error", "Please enter a valid password!");
			return "signup";
		}

		if(!isVerify) 

		{
			model.addAttribute("username", username);
			model.addAttribute("verify_error", "Passwords entered do not match!");
			return "signup";
		}

		model.addAttribute("username_error", "Something went wrong!");		
		return "signup";
	}





	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {

		// TODO - implement login

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User existingUser = userDao.findByUsername(username);

		
		try {
			
			if(existingUser.isMatchingPassword(password)){

				HttpSession session = request.getSession();
				setUserInSession(session, existingUser);
				return "redirect:blog/newpost";
			} else {
				model.addAttribute("error", "Invalid password!");
				return "login";
			}
		}

		catch(NullPointerException e)
		{

			model.addAttribute("error", "Invalid username!");
			return "login";
		}

	}



	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
		request.getSession().invalidate();
		return "redirect:/";
	}
}
