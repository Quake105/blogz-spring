package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {

		// TODO - implement newPost

		String title = request.getParameter("title");
		String body = request.getParameter("body");
		Integer userId = (Integer) request.getSession().getAttribute(AbstractController.userSessionKey);

		boolean isTitle = false;
		if(title != null && !title.isEmpty()){
			isTitle = true;
		}

		boolean isBody = false;
		if(title != null && !title.isEmpty()){
			isBody = true;
		}
		int postUid = 0;
		if(isTitle && isBody){
			Post newPost = new Post(title, body, userDao.findByUid(userId));
			postDao.save(newPost);
			
			 postUid = newPost.getUid();
		} 
		else if(!isTitle && !isBody){
			model.addAttribute("title_error", "Please give a title");
			model.addAttribute("post_error", "Please give a title");
		}
		else if(!isTitle){
			model.addAttribute("title_error", "Please give a title");
		}
		else if(!isBody){
			model.addAttribute("post_error", "Please give a title");
		}

		return "redirect:/blog/" + userDao.findByUid(userId).getUsername() + "/" + postUid ;  // TODO - this redirect should go to the new post's page  		
	}

	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {

		// TODO - implement singlePost
		List<Post> posts  =  postDao.findByUid(uid);
		User users = userDao.findByUsername(username);
		
		model.addAttribute("posts", posts);
		model.addAttribute("users", users);
		return "post";
	}

	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {

		// TODO - implement userPosts
		
		User thisUser = userDao.findByUsername(username);
		
		List<Post> userPosts = thisUser.getPosts();
		String postUsername = thisUser.getUsername();
		model.addAttribute("postUsername", postUsername);
		model.addAttribute("userPosts", userPosts);

		return "blog";
	}

}
