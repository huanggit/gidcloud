package com.cpsdna.gidCloud.web.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface DeviceMapper {

	@Select("<script>SELECT device_id deviceId, engine_displacement engineDisplacement "
			+ "FROM cloud.device WHERE user_id = #{userId} "
			+ "<if test=\"deviceIdKeyword != null and deviceIdKeyword != '' \"> "
			+ "AND device_id like '%${deviceIdKeyword}%' "
			+ "</if>"
			+ "LIMIT #{page.start},#{page.size}"
			+ "</script>")
	List<Map<String, Object>> getDevicesByUserId(
			@Param("userId") String userId,
			@Param("deviceIdKeyword") String deviceIdKeyword, @Param("page") Page page);
	
	
	@Select("<script>SELECT COUNT(1) FROM cloud.device WHERE user_id = #{userId} "
			+ "<if test=\"deviceIdKeyword != null and deviceIdKeyword != '' \"> "
			+ "AND device_id like '%${deviceIdKeyword}' "
			+ "</if>"
			+ "</script>")
	int getDevicesNoByUserId(
			@Param("userId") String userId,
			@Param("deviceIdKeyword") String deviceIdKeyword);
}
