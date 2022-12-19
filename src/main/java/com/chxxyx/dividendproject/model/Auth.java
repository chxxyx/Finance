package com.chxxyx.dividendproject.model;

import com.chxxyx.dividendproject.persist.entity.MemberEntity;
import java.util.List;
import lombok.Data;

public class Auth {

	@Data
	public static class SignIn{ // 로그인
		private String username;
		private String password;
	}

	@Data
	public static class SignUp{ // 회원가입
		private String username;
		private String password;
		private List<String> roles;

		public MemberEntity toEntity() {
			return MemberEntity.builder()
								.username(this.username)
								.password(this.password)
								.roles(this.roles)
								.build();
		}

	}
}
