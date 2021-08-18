package com.isuper.eden.eve.boot.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

public class JsonMapper extends ObjectMapper {

  private static final long serialVersionUID = 1L;

  private static JsonMapper mapper;

  public JsonMapper() {
    this(JsonInclude.Include.NON_EMPTY);
  }

  public JsonMapper(JsonInclude.Include include) {
    // 设置输出时包含属性的风格
    if (include != null) {
      this.setSerializationInclusion(include);
    }
    // 允许单引号、允许不带引号的字段名称
    this.enableSimple();
    // this.enableJavaTime();  设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
    this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    // 空值处理为空串
    this.getSerializerProvider()
        .setNullValueSerializer(
            new JsonSerializer<Object>() {
              @Override
              public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                  throws IOException, JsonProcessingException {
                jgen.writeString("");
              }
            });
  }

  /**
   * getInstance
   *
   * <p>获取JsonMapper实例<br>
   *
   * @return JsonMapper
   */
  public static JsonMapper getInstance() {
    if (mapper == null) {
      mapper = new JsonMapper().enableSimple();
    }
    return mapper;
  }

  /**
   * nonDefaultMapper
   *
   * <p>创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。<br>
   *
   * @return JsonMapper
   */
  public static JsonMapper nonDefaultMapper() {
    if (mapper == null) {
      mapper = new JsonMapper(JsonInclude.Include.NON_DEFAULT);
    }
    return mapper;
  }

  /**
   * toJson
   *
   * <p>Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]". <br>
   *
   * @param object object
   * @return Json字符串
   */
  public String toJson(Object object) {

    try {
      return this.writeValueAsString(object);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * toJson
   *
   * <p>Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]". <br>
   *
   * @param object object
   * @param viewType viewType
   * @return Json字符串
   */
  public String toJson(Object object, Class<?> viewType) {

    try {
      return this.writerWithView(viewType).writeValueAsString(object);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * fromJson2List
   *
   * <p>Json转换成为List <br>
   *
   * @param jsonString object
   * @param <T> clazz
   * @return 泛型
   */
  public <T> List<T> fromJson2List(String jsonString, Class<T> clazz) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }
    try {
      return this.readValue(jsonString, new TypeReference<List<T>>() {});
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * fromJson
   *
   * <p>反序列化POJO或简单Collection如List<br>
   *
   * <p>如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
   *
   * <p>如需反序列化复杂Collection如List, 请使用fromJson(String,JavaType)
   *
   * @see #fromJson(String, JavaType)
   * @param jsonString object
   * @param clazz clazz
   * @return T
   */
  public <T> T fromJson(String jsonString, Class<T> clazz) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }
    try {
      return this.readValue(jsonString, clazz);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * fromJson
   *
   * <p>反序列化POJO或简单Collection如List <br>
   *
   * <p>如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
   *
   * <p>如需反序列化复杂Collection如List, 请使用fromJson(String,JavaType)
   *
   * @see #fromJson(String, JavaType)
   * @param jsonString object
   * @param clazz clazz
   * @param viewType viewType
   * @return List
   */
  public <T> T fromJson(String jsonString, Class<T> clazz, Class<?> viewType) {

    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }
    try {
      return this.readerWithView(viewType).forType(clazz).readValue(jsonString);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * fromJson
   *
   * <p>反序列化复杂Collection如List, 先使用函數createCollectionType构造类型,然后调用本函数 <br>
   *
   * @see #createCollectionType(Class, Class...)
   * @param jsonString object
   * @param javaType javaType
   * @return 结果
   */
  @SuppressWarnings("unchecked")
  public <T> T fromJson(String jsonString, JavaType javaType) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }
    try {
      return (T) this.readValue(jsonString, javaType);
    } catch (IOException e) {
      return null;
    }
  }

  public <T> T fromJson(String jsonString, JavaType javaType, Class<?> viewType) {
    if (StringUtils.isEmpty(jsonString)) {
      return null;
    }
    try {
      return this.readerWithView(viewType).forType(javaType).readValue(jsonString);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * createCollectionType
   *
   * <p>構造泛型的Collection <br>
   * Type如: ArrayList, 则调用constructCollectionType(ArrayList.class,MyBean.class) HashMap,
   * 则调用(HashMap.class,String.class, MyBean.class)
   *
   * @param collectionClass collectionClass
   * @param elementClasses elementClasses
   * @return 结果
   */
  public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
    return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
  }

  /**
   * update
   *
   * <p>當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性. <br>
   *
   * @param jsonString jsonString
   * @param object object
   * @return T结果
   */
  @SuppressWarnings("unchecked")
  public <T> T update(String jsonString, T object) {
    try {
      return (T) this.readerForUpdating(object).readValue(jsonString);
    } catch (JsonProcessingException e) {
    }
    return null;
  }

  /**
   * toJsonP
   *
   * <p>輸出JSONP格式數據. <br>
   *
   * @param functionName functionName
   * @param object object
   * @return JsonP字符串
   */
  public String toJsonP(String functionName, Object object) {
    return toJson(new JSONPObject(functionName, object));
  }

  /**
   * enableEnumUseToString
   *
   * <p>設定是否使用Enum的toString函數來讀寫Enum, 為False時時使用Enum的name()函數來讀寫Enum, 默認為False. 注意本函數一定要在Mapper創建後,
   * 所有的讀寫動作之前調用. <br>
   *
   * @return JsonMapper
   */
  public JsonMapper enableEnumUseToString() {
    this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    return this;
  }

  /**
   * enableJaxbAnnotation
   *
   * <p>支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。 默认会先查找jaxb的annotation，如果找不到再找jackson的。
   * <br>
   *
   * @return JsonMapper
   */
  public JsonMapper enableJaxbAnnotation() {
    JaxbAnnotationModule module = new JaxbAnnotationModule();
    this.registerModule(module);
    return this;
  }

  public JsonMapper enableJavaTime() {
    JavaTimeModule module = new JavaTimeModule();
    this.registerModule(module);
    return this;
  }

  /**
   * enableSimple
   *
   * <p>允许单引号 允许不带引号的字段名称 <br>
   *
   * @return JsonMapper
   */
  public JsonMapper enableSimple() {
    this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    return this;
  }

  /**
   * getMapper
   *
   * <p>取出Mapper做进一步的设置或使用其他序列化API.<br>
   *
   * @return JsonMapper
   */
  public ObjectMapper getMapper() {
    return this;
  }

  /**
   * toJsonString
   *
   * <p>转换为JSON字符串<br>
   *
   * @param object object
   * @return String
   */
  public static String toJsonString(Object object) {
    return JsonMapper.getInstance().toJson(object);
  }
}
