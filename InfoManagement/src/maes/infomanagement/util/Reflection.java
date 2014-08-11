package maes.infomanagement.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
	/**
	 * 得到某个对象的公共属�?
	 * 
	 * @param owner
	 *            , fieldName
	 * @return 该属性对�?
	 * @throws Exception
	 * 
	 */
	public Object getProperty(Object owner, String fieldName) throws Exception {
		Class ownerClass = owner.getClass();

		Field field = ownerClass.getField(fieldName);

		Object property = field.get(owner);

		return property;
	}

	/**
	 * 得到某类的静态公共属�?
	 * 
	 * @param className
	 *            类名
	 * @param fieldName
	 *            属�?�名
	 * @return 该属性对�?
	 * @throws Exception
	 */
	public Object getStaticProperty(String className, String fieldName)
			throws Exception {
		Class ownerClass = Class.forName(className);

		Field field = ownerClass.getField(fieldName);

		Object property = field.get(ownerClass);

		return property;
	}

	/**
	 * 执行某对象方�?
	 * 
	 * @param owner
	 *            对象
	 * @param methodName
	 *            方法�?
	 * @param args
	 *            参数
	 * @return 方法返回�?
	 * @throws Exception
	 */
	public Object invokeMethod(Object owner, String methodName, Object[] args)
			throws Exception {

		Class ownerClass = owner.getClass();

		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(owner, args);
	}

	/**
	 * 执行某类的静态方�?
	 * 
	 * @param className
	 *            类名
	 * @param methodName
	 *            方法�?
	 * @param args
	 *            参数数组
	 * @return 执行方法返回的结�?
	 * @throws Exception
	 */
	public Object invokeStaticMethod(String className, String methodName,
			Object[] args) throws Exception {
		Class ownerClass = Class.forName(className);

		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(null, args);
	}

	/**
	 * 新建实例
	 * 
	 * @param className
	 *            类名
	 * @param args
	 *            构�?�函数的参数 如果无构造参数，args 填写�? null
	 * @return 新建的实�?
	 * @throws Exception
	 */
	public Object newInstance(String className, Object[] args, Class[] argsType)
			throws NoSuchMethodException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Class newoneClass = Class.forName(className);

		if (args == null) {
			return newoneClass.newInstance();

		} else {
			// Class[] argsClass = new Class[args.length];
			//
			// for (int i = 0, j = args.length; i < j; i++) {
			// argsClass[i] = args[i].getClass();
			// }
			//
			// Constructor cons = newoneClass.getConstructor(argsClass);
			Constructor cons = newoneClass.getConstructor(argsType);

			return cons.newInstance(args);
		}

	}

	/**
	 * 是不是某个类的实�?
	 * 
	 * @param obj
	 *            实例
	 * @param cls
	 *            �?
	 * @return 如果 obj 是此类的实例，则返回 true
	 */
	public boolean isInstance(Object obj, Class cls) {
		return cls.isInstance(obj);
	}

	/**
	 * 得到数组中的某个元素
	 * 
	 * @param array
	 *            数组
	 * @param index
	 *            索引
	 * @return 返回指定数组对象中索引组件的�?
	 */
	public Object getByArray(Object array, int index) {
		return Array.get(array, index);
	}

	public Class<?> GetClassListByPackage(String pPackage) {
		Package _Package = Package.getPackage(pPackage);
		Class<?> _List = _Package.getClass();

		return _List;
	}
}
