package com.lightreader.bzz.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import android.content.Context;
import android.util.DisplayMetrics;


/**
 * Bean工具类
 *
 * @author EwinLive
 */
public abstract class BeanTools {
    /**
     * 获取第一个泛型类
     */
    public static Class<?> getGenericClass(Class<?> clazz) {
        return getGenericClass(clazz, 0);
    }

    /**
     * 获取泛型类
     */
    public static Class<?> getGenericClass(Class<?> clazz, int index) throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size of Parameterized Type: " + params.length);
        }
        return (Class<?>) params[index];
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            //logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    /**
     * 强行设置Field可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField.
     * <p/>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    protected static Field getDeclaredField(final Object object, final String fieldName) {
        //Assert.notNull(object, "object不能为空");
        //Assert.hasText(fieldName, "fieldName");
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 强行获取私有属性的值
     */
    public static Object getPrivateProperty(Object object, String propertyName) throws IllegalAccessException, NoSuchFieldException {
        //Assert.notNull(object);
        //Assert.hasText(propertyName);
        Field field = object.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        return field.get(object);
    }

    /**
     * 获取所有字段,返回Map对象
     *
     * @param entityClass 实体的类型
     * @return data
     * 返回包含两个数组的HashMap，可参考以下使用方法：
     * String[] fieldName = (String[]) data.get("fieldName");
     * Class<?>[] fieldType = (Class<?>[]) data.get("fieldType");
     */
    public static HashMap<Object, Object> getAllFiled(Class<?> entityClass) {
        HashMap<Object, Object> data = new HashMap<Object, Object>();

        Field[] fields = entityClass.getDeclaredFields();
        String[] fieldName = new String[fields.length];
        Class<?>[] fieldType = new Class<?>[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fieldName[i] = fields[i].getName();//组装名称数组
            fieldType[i] = fields[i].getType();//组装类型数组
        }

        data.put("fieldName", fieldName);
        data.put("fieldType", fieldType);

        return data;
    }
    
    
    
    /**
     * 获取当前设备的屏幕的宽和高
     * @param context
     * @return
     */
    public static int[] getDeviceWidthAndHeight(Context context){
    	int[] intArray = new int[2];
    	DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    	int height = displayMetrics.heightPixels;
    	int width = displayMetrics.widthPixels;
    	intArray[0] = width;
        intArray[1] = height;
        return intArray;
    }
    
    
	/**
	 * 根据手机的分辨率从dp的单位转换成px(像素)
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从px(像素)的单位转换成dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
    
}
