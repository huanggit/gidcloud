package com.cpsdna.gidCloud.web.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * Json工具类
 * 
 * @author sll
 *
 */
public class JsonUtil
{

	/**
	 * jackson的ObjectMapper对象，用于pojo和json之间相互转化
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	public static <T> T readValue(String jsonStr, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException
	{
		return mapper.readValue(jsonStr, valueType);
	}

	public static void writeValue(Writer w, Object value) throws JsonGenerationException, JsonMappingException, IOException
	{
		mapper.writeValue(w, value);
		w.close();
	}

	public static void writeValue(OutputStream out, Object value) throws JsonGenerationException, JsonMappingException, IOException
	{
		mapper.writeValue(out, value);
		out.close();
	}

	public static String writeValueAsString(Object value) throws IOException
	{
		return mapper.writeValueAsString(value);
	}

	public static Map<?, ?> readValueAsMap(String jsonStr) throws JsonParseException, JsonMappingException, IOException
	{
		return mapper.readValue(jsonStr, Map.class);
	}


	
}
