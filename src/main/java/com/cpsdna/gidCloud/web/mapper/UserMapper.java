package com.cpsdna.gidCloud.web.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface UserMapper {
	@Select("SELECT user_id userId, password, username, type, level, email, org, phone, bbs_uid bbsUid "
			+ "FROM cloud.user WHERE username = #{name} AND status = 1")
	Map<String, Object> getUserByName(String name);

	@Select("SELECT user_id userId, password, username, type, level, email, org, phone "
			+ "FROM cloud.user WHERE user_id = #{userId}")
	Map<String, Object> getUserById(String userId);
	
	@Select("SELECT user_id userId, password, username, type, level, email, org, phone "
			+ "FROM cloud.user WHERE email = #{email}")
	Map<String, Object> getUserByEmail(String email);

	@Select("SELECT count(1) FROM cloud.user WHERE username = #{username}")
	int getUserCountByName(String username);

	@Select("SELECT count(1) FROM cloud.user WHERE email = #{email}")
	int getUserCountByEmail(String email);

	@Update("UPDATE cloud.user SET org = #{org}, phone = #{phone} WHERE user_id = #{userId}")
	void updateUserById(@Param("userId") String userId,
			@Param("org") String org, @Param("phone") String phone);
	
	@Update("UPDATE cloud.user SET password = #{password} WHERE user_id = #{userId}")
	void updatePasswordById(@Param("userId") String userId,
			@Param("password") String password);
	
	@Update("UPDATE cloud.user SET status = #{status} WHERE user_id = #{userId}")
	void updateStatusById(@Param("userId") String userId,
			@Param("status") String status);
	
	@Insert("INSERT INTO cloud.user (user_id, username, password, email, bbs_uid, recd_time) "
			+ "VALUES (#{userId}, #{username}, #{password}, #{email}, #{bbsUid}, NOW())")
	void insertUser(@Param("userId") String userId,
			@Param("username") String username,
			@Param("password") String password, @Param("email") String email, @Param("bbsUid") String bbsUid);
}
