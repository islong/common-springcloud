package com.dh.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 *  json数据转换工具包
 *  @author caisj
 */
public class GsonUtil {
	private static Gson gson = null;
	static {
		if (gson == null) {
			GsonBuilder gb =new GsonBuilder();
			gb.disableHtmlEscaping();
			gson = gb.create();
		}
	}

	private GsonUtil() {
	}

	/**
	 * 转成json
	 * 
	 * @param object
	 * @return
	 */
	public static <T> Map<String, T> toMap(Object object) {
		String gsonString = null;
		if (gson != null) {
			gsonString = gson.toJson(object);
		}
		return toMap(gsonString);
	}
	/**
	 * 转成json
	 * 
	 * @param object
	 * @return
	 */
	public static String toString(Object object) {
		String gsonString = null;
		if (gson != null) {
			gsonString = gson.toJson(object);
		}
		return gsonString;
	}

	/**
	 * 转成bean
	 * 
	 * @param gsonString
	 * @param cls
	 * @return
	 */
	public static <T> T toObject(String gsonString, Class<T> cls) {
		T t = null;
		if (gson != null) {
			t = gson.fromJson(gsonString, cls);
		}
		return t;
	}

	/**
	 * 转成list
	 * 
	 * @param gsonString
	 * @param cls
	 * @return
	 */
	public static <T> List<T> toList(String gsonString, Class<T> cls) {
		List<T> mList = new ArrayList<T>();
		JsonArray array = new JsonParser().parse(gsonString).getAsJsonArray();
		for (final JsonElement elem : array) {
			mList.add(gson.fromJson(elem, cls));
		}
		return mList;
	}

	/**
	 * 转成list中有map的
	 * 
	 * @param gsonString
	 * @return
	 */
	public static <T> List<Map<String, T>> toListMap(String gsonString) {
		List<Map<String, T>> list = null;
		gson=new GsonBuilder().disableHtmlEscaping()
				/**
				 * 重写map的反序列化
				 */
				.registerTypeAdapter(new TypeToken<Map<String, T>>() {
				}.getType(), new MapTypeAdapter()).create();
		if (gson != null) {
			list = gson.fromJson(gsonString, new TypeToken<List<TreeMap<String, T>>>() {
			}.getType());
		}
		return list;
	}

	/**
	 * 转成map的
	 * 
	 * @param gsonString
	 * @return
	 */
	public static <T> Map<String, T> toMap(String gsonString) {
		Map<String, T> map = null;
		gson=new GsonBuilder().disableHtmlEscaping()
				.registerTypeAdapter(new TypeToken<Map<String, T>>() {
				}.getType(), new MapTypeAdapter()).create();
		if (gson != null) {
			map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
			}.getType());
		}
		return map;
	}

	public static class MapTypeAdapter extends TypeAdapter<Object> {

		@Override
		public Object read(JsonReader in) throws IOException {
			JsonToken token = in.peek();
			switch (token) {
			case BEGIN_ARRAY:
				List<Object> list = new ArrayList<Object>();
				in.beginArray();
				while (in.hasNext()) {
					list.add(read(in));
				}
				in.endArray();
				return list;

			case BEGIN_OBJECT:
				Map<String, Object> map = new LinkedTreeMap<String, Object>();
				in.beginObject();
				while (in.hasNext()) {
					map.put(in.nextName(), read(in));
				}
				in.endObject();
				return map;

			case STRING:
				return in.nextString();

			case NUMBER:
				/**
				 * 改写数字的处理逻辑，将数字值分为整型与浮点型。
				 */
				double dbNum = in.nextDouble();

				// 数字超过long的最大值，返回浮点类型
				if (dbNum > Long.MAX_VALUE) {
					return dbNum;
				}

				// 判断数字是否为整数值
				long lngNum = (long) dbNum;
				if (dbNum == lngNum) {
					return lngNum;
				} else {
					return dbNum;
				}

			case BOOLEAN:
				return in.nextBoolean();

			case NULL:
				in.nextNull();
				return null;

			default:
				throw new IllegalStateException();
			}
		}

		@Override
		public void write(JsonWriter out, Object value) throws IOException {
		}

	}
}
