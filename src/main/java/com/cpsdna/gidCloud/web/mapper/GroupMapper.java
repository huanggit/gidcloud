package com.cpsdna.gidCloud.web.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface GroupMapper {

	@Select("<script>SELECT group_id groupId, group_name groupName, remark "
			+ "FROM cloud.device_group WHERE user_id = #{userId} "
			+ "<if test=\"groupNameKeyword != null and groupNameKeyword != '' \"> "
			+ "AND group_name like '%${groupNameKeyword}%' " + "</if>"
			+ "<if test=\"groupIdKeyword != null and groupIdKeyword != '' \"> "
			+ "AND group_id like '%${groupIdKeyword}%' " + "</if> "
			+ "LIMIT #{page.start},#{page.size}" + "</script>")
	List<Map<String, Object>> getGroupsByUser(@Param("userId") String userId,
			@Param("groupNameKeyword") String groupNameKeyword,
			@Param("groupIdKeyword") String groupIdKeyword,
			@Param("page") Page page);

	@Select("<script>SELECT count(1) "
			+ "FROM cloud.device_group WHERE user_id = #{userId} "
			+ "<if test=\"groupNameKeyword != null and groupNameKeyword != '' \"> "
			+ "AND group_name like '%${groupNameKeyword}%' " + "</if>"
			+ "<if test=\"groupIdKeyword != null and groupIdKeyword != '' \"> "
			+ "AND group_id like '%${groupIdKeyword}%' " + "</if> "
			+ "</script>")
	int getGroupsNoByUser(@Param("userId") String userId,
			@Param("groupNameKeyword") String groupNameKeyword,
			@Param("groupIdKeyword") String groupIdKeyword);

	@Select("<script>"
			+ "SELECT group_id groupId, group_name groupName, remark "
			+ "FROM cloud.device_group WHERE user_id = #{userId} "
			+ "<if test=\"deviceIdKeyword != null and deviceIdKeyword != '' \"> "
			+ "AND group_id IN( SELECT g.group_id groupId "
			+ "FROM cloud.device_group g, cloud.group_device_map map "
			+ "WHERE g.user_id = #{userId} "
			+ "AND g.group_id = map.group_id "
			+ "AND map.device_id like '%${deviceIdKeyword}%') "
			+ "</if> "
			+ "LIMIT #{page.start},#{page.size}" + "</script>")
	List<Map<String, Object>> getGroupsByUserAndDevice(
			@Param("userId") String userId,
			@Param("deviceIdKeyword") String deviceIdKeyword,
			@Param("page") Page page);

	@Select("<script>SELECT count(1) "
			+ "FROM cloud.device_group g, cloud.group_device_map map "
			+ "WHERE g.user_id = #{userId} "
			+ "AND g.group_id = map.group_id "
			+ "<if test=\"deviceIdKeyword != null and deviceIdKeyword != '' \"> "
			+ "AND map.device_id like '%${deviceIdKeyword}%' " + "</if> "
			+ "</script>")
	int getGroupsNoByUserAndDevice(@Param("userId") String userId,
			@Param("deviceIdKeyword") String deviceIdKeyword);

	@Insert("INSERT INTO cloud.device_group (group_id, group_name, user_id, recd_time, remark) VALUES (#{groupId}, #{groupName}, #{userId}, NOW(), #{remark})")
	void insertDeviceGroup(@Param("groupId") String groupId,
			@Param("groupName") String groupName,
			@Param("userId") String userId, @Param("remark") String remark);

	@Update("UPDATE cloud.device_group SET group_name = #{groupName} and remark = #{remark} where group_id = #{groupId}")
	void updateDeviceGroup(@Param("groupId") String groupId,
			@Param("groupName") String groupName, @Param("remark") String remark);

	@Delete("DELETE FROM cloud.device_group WHERE group_id = #{groupId}")
	void deleteDeviceGroup(@Param("groupId") String groupId);

	@Select("SELECT device_id deviceId FROM cloud.group_device_map WHERE group_id = #{groupId}")
	List<String> getDevicesByGroup(String groupId);

	@Insert("<script>INSERT INTO cloud.group_device_map (recd_id, group_id, device_id, recd_time) VALUES "
			+ "<foreach item='item' collection='list' separator=','>(#{item.id},#{groupId},#{item.deviceId},NOW())</foreach></script>")
	void insertDevicesToGroup(@Param("groupId") String groupId,
			@Param("list") List<Map<String, Object>> devicesList);

	@Delete("<script>DELETE FROM cloud.group_device_map WHERE group_id = #{groupId} AND device_id IN "
			+ "<foreach item='item' collection='list' separator=',' open='(' close=')'>#{item}</foreach></script>")
	void deleteDevicesFromGroup(@Param("groupId") String groupId,
			@Param("list") List<String> devicesList);
}
