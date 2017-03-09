package com.ozge.movieRecommender.model.dto;

import com.ozge.movieRecommender.model.User;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by ozge on 09.03.2017.
 */
public class ProfileInfoDto {

	private Long id;

	@NotEmpty
	private String username;

	private String email;

	private int age;

	public ProfileInfoDto() {
	}

	public ProfileInfoDto(User user) {
		this.setId(user.getId());
		this.setUsername(user.getUsername());
		this.setAge(user.getAge());
		this.setEmail(user.getEmail());
	}

	public User toUser(User user){
		user.setUsername(this.getUsername());
		user.setEmail(this.getEmail());
		user.setAge(this.getAge());
		return user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
