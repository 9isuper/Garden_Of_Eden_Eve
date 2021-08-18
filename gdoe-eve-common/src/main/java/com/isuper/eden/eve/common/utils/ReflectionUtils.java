package com.isuper.eden.eve.common.utils;

import com.isuper.eden.eve.common.constant.SystemConstant;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.*;


public class ReflectionUtils {

  private static final String SETTER_PREFIX = "set";

  private static final String GETTER_PREFIX = "get";

  private static final String CGLIB_CLASS_SEPARATOR = "$$";

  private static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

  public static Object invokeGetter(Object obj, String propertyName) {
    Object object = obj;
    for (String name : StringUtils.split(propertyName, SystemConstant.SEPARATOR_POINT)) {
      String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
      object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
    }
    return object;
  }

  public static void invokeSetter(Object obj, String propertyName, Object value) {
    Object object = obj;
    String[] names = StringUtils.split(propertyName, ".");
    for (int i = 0; i < names.length; i++) {
      if (i < names.length - 1) {
        String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
        object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
      } else {
        String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
        invokeMethodByName(object, setterMethodName, new Object[] {value});
      }
    }
  }

  public static Object getFieldValue(final Object obj, final String fieldName) {
    Field field = getAccessibleField(obj, fieldName);

    if (field == null) {
      throw new IllegalArgumentException(
          "Could not find field [" + fieldName + "] on target [" + obj + "]");
    }

    Object result = null;
    try {
      result = field.get(obj);
    } catch (IllegalAccessException e) {
      log.error("不可能抛出的异常{}", e.getMessage());
    }
    return result;
  }

  /**
   * setFieldValue 直接设置对象属性值.
   *
   * <p>直接设置对象属性值<br>
   * 无视private/protected修饰符, 不经过setter函数.
   *
   * @param obj 对象
   * @param fieldName 字段名称
   * @param value 值
   */
  public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
    Field field = getAccessibleField(obj, fieldName);

    if (field == null) {
      throw new IllegalArgumentException(
          "Could not find field [" + fieldName + "] on target [" + obj + "]");
    }

    try {
      field.set(obj, value);
    } catch (IllegalAccessException e) {
      log.error("不可能抛出的异常:{}", e.getMessage());
    }
  }

  /**
   * invokeMethod 直接执行方法.
   *
   * <p>直接执行方法.<br>
   * 无视private/protected修饰符. 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用，同时匹配方法名+参数类型，
   *
   * @param obj 对象
   * @param methodName 对象
   * @param parameterTypes 调用方法的参数
   * @param args 对象
   * @return 对象,
   */
  public static Object invokeMethod(
      final Object obj,
      final String methodName,
      final Class<?>[] parameterTypes,
      final Object[] args) {
    Method method = getAccessibleMethod(obj, methodName, parameterTypes);
    if (method == null) {
      throw new IllegalArgumentException(
          "Could not find method [" + methodName + "] on target [" + obj + "]");
    }

    try {
      return method.invoke(obj, args);
    } catch (Exception e) {
      throw convertReflectionExceptionToUnchecked(e);
    }
  }

  /**
   * invokeMethodByName
   *
   * <p>直接调用对象方法<br>
   * 视private/protected修饰符， 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
   * 只匹配函数名，如果有多个同名函数调用第一个。
   *
   * @param obj Object
   * @param methodName methodName
   * @param args args
   * @return Object
   */
  public static Object invokeMethodByName(
      final Object obj, final String methodName, final Object[] args) {
    Method method = getAccessibleMethodByName(obj, methodName);
    if (method == null) {
      throw new IllegalArgumentException(
          "Could not find method [" + methodName + "] on target [" + obj + "]");
    }

    try {
      return method.invoke(obj, args);
    } catch (Exception e) {
      throw convertReflectionExceptionToUnchecked(e);
    }
  }

  /**
   * getAccessibleField
   *
   * <p>循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问<br>
   * 如向上转型到Object仍无法找到, 返回null.
   *
   * @param obj Object
   * @param fieldName fieldName
   * @return Field
   */
  public static Field getAccessibleField(final Object obj, final String fieldName) {
    Validate.notNull(obj, "object can't be null");
    Validate.notBlank(fieldName, "fieldName can't be blank");
    for (Class<?> superClass = obj.getClass();
        superClass != Object.class;
        superClass = superClass.getSuperclass()) {
      try {
        Field field = superClass.getDeclaredField(fieldName);
        makeAccessible(field);
        return field;
      } catch (NoSuchFieldException e) { // NOSONAR
        // Field不在当前类定义,继续向上转型
        continue; // new add
      }
    }
    return null;
  }

  /**
   * getAccessibleMethod
   *
   * <p>循环向上转型, 获取对象的DeclaredMethod, 并强制设置为可访问<br>
   * 如向上转型到Object仍无法找到, 返回null. 匹配函数名+参数类型。
   *
   * <p>用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
   *
   * @param obj Object
   * @param methodName methodName
   * @param parameterTypes parameterTypes
   * @return Method
   */
  public static Method getAccessibleMethod(
      final Object obj, final String methodName, final Class<?>... parameterTypes) {
    Validate.notNull(obj, "object can't be null");
    Validate.notBlank(methodName, "methodName can't be blank");

    for (Class<?> searchType = obj.getClass();
        searchType != Object.class;
        searchType = searchType.getSuperclass()) {
      try {
        Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
        makeAccessible(method);
        return method;
      } catch (NoSuchMethodException e) {
        // Method不在当前类定义,继续向上转型
        continue; // new add
      }
    }
    return null;
  }

  /**
   * getAccessibleMethodByName
   *
   * <p>循环向上转型, 获取对象的DeclaredMethod, 并强制设置为可访问<br>
   * 如向上转型到Object仍无法找到, 返回null. 只匹配函数名。
   *
   * <p>用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
   *
   * @param obj Object
   * @param methodName methodName
   * @return Method
   */
  public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
    Validate.notNull(obj, "object can't be null");
    Validate.notBlank(methodName, "methodName can't be blank");

    for (Class<?> searchType = obj.getClass();
        searchType != Object.class;
        searchType = searchType.getSuperclass()) {
      Method[] methods = searchType.getDeclaredMethods();
      for (Method method : methods) {
        if (method.getName().equals(methodName)) {
          makeAccessible(method);
          return method;
        }
      }
    }
    return null;
  }

  /**
   * makeAccessible
   *
   * <p>改变private/protected的方法为public<br>
   * 尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
   *
   * @param method method
   */
  public static void makeAccessible(Method method) {
    if ((!Modifier.isPublic(method.getModifiers())
            || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
        && !method.isAccessible()) {
      method.setAccessible(true);
    }
  }

  /**
   * makeAccessible
   *
   * <p>改变private/protected的方法为public<br>
   * 尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
   *
   * @param field field
   */
  public static void makeAccessible(Field field) {
    if ((!Modifier.isPublic(field.getModifiers())
            || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
            || Modifier.isFinal(field.getModifiers()))
        && !field.isAccessible()) {
      field.setAccessible(true);
    }
  }


  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> Class<T> getClassGenricType(final Class clazz) {
    return getClassGenricType(clazz, 0);
  }

  /**
   * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
   *
   * <p>如public UserDao extends HibernateDao
   *
   * @param clazz clazz The class to introspect
   * @param index the Index of the generic ddeclaration,start from 0.
   * @return the index generic declaration, or Object.class if cannot be determined
   */
  @SuppressWarnings("rawtypes")
  public static Class getClassGenricType(final Class clazz, final int index) {

    Type genType = clazz.getGenericSuperclass();

    if (!(genType instanceof ParameterizedType)) {
      log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
      return Object.class;
    }

    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

    if (index >= params.length || index < 0) {
      log.warn(
          "Index: "
              + index
              + ", Size of "
              + clazz.getSimpleName()
              + "'s Parameterized Type: "
              + params.length);
      return Object.class;
    }
    if (!(params[index] instanceof Class)) {
      log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
      return Object.class;
    }

    return (Class) params[index];
  }

  @SuppressWarnings("rawtypes")
  public static Class<?> getUserClass(Object instance) {
    if (ObjectUtils.isEmpty(instance)) {
      throw new IllegalStateException("Instance must not be null");
    }
    Class clazz = instance.getClass();
    if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
      Class<?> superClass = clazz.getSuperclass();
      if (superClass != null && !Object.class.equals(superClass)) {
        return superClass;
      }
    }
    return clazz;
  }

  /**
   * convertReflectionExceptionToUnchecked
   *
   * <p>将反射时的checked exception转换为unchecked exception.<br>
   *
   * @param e Exception
   * @return RuntimeException
   */
  public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
    if (e instanceof IllegalAccessException
        || e instanceof IllegalArgumentException
        || e instanceof NoSuchMethodException) {
      return new IllegalArgumentException(e);
    } else if (e instanceof InvocationTargetException) {
      return new RuntimeException(((InvocationTargetException) e).getTargetException());
    } else if (e instanceof RuntimeException) {
      return (RuntimeException) e;
    }
    return new RuntimeException("Unexpected Checked Exception.", e);
  }
}
