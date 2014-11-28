package com.lightreader.bzz.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Point;
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
     * 获取当前设备的屏幕的中间区域的一个正方形[左上,右上,右下,左下]四个点,判断传入的坐标点是否位于整个区间内
     * @param context
     * @return
     */
    public static boolean getDevicePointsFlag(Context context,float x,float y){
    	DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    	int height = displayMetrics.heightPixels / 2;
    	int width = displayMetrics.widthPixels / 2;

    	float a1_x = width - width / 4 ;
    	float a1_y  = height - height / 4;

    	float a2_x = width + width / 4 ;
    	float a2_y = height - height / 4;

    	float a3_x = width + width / 4 ;
    	float a3_y = height + height / 4;
    	
    	float a4_x = width - width / 4 ;
    	float a4_y = height + height / 4;
    	
    	if(a1_x < x && a2_x > x && a3_x > x && a4_x < x){
    		if(a1_y < y && a2_y < y && a3_y > y && a4_y > y){
    			return true;
    		}
    	}
    	return false;
    }
    
    
    
    

	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	
	/**
	 * 获取系统时间是否是24小时制
	 * @param context
	 * @return
	 */
	private static boolean isOrNot24Time(Context context){
		ContentResolver cv = context.getContentResolver();  
	    String strTimeFormat = android.provider.Settings.System.getString(cv,android.provider.Settings.System.TIME_12_24);  
	    if(strTimeFormat.equals("24")){  
	       //Log.i("activity","24");
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	/**
	 * 返回最终的系统当前时间     格式: 13:50
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getSystemCurrentTime(Context context){
		Calendar c = Calendar.getInstance();
		//取得系统日期:
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		//取得系统时间：
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		boolean timeType = isOrNot24Time(context);
		if(!timeType){
			if(hour >= 12){
				hour -= 12;
			}
		}
		return String.valueOf(hour).concat(":").concat(minute < 10 ? "0"+String.valueOf(minute) : String.valueOf(minute));
	}
	
	/**
	 * 返回最终的系统当前时间     格式: 13:50
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getSystemCurrentTime2(Context context){
		Calendar c = Calendar.getInstance();
		//取得系统日期:
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		//取得系统时间：
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int Second = c.get(Calendar.SECOND);
		
		boolean timeType = isOrNot24Time(context);
		if(!timeType){
			if(hour >= 12){
				hour -= 12;
			}
		}
		return String.valueOf(hour).concat(":").concat(minute < 10 ? "0"+String.valueOf(minute) : String.valueOf(minute)).concat(":"+Second);
	}
	
	
    /**
     * 根据Integer数组,获取每个数之间的差(平均)
     * @param list
     * @return
     */
	public static Integer getAvgNumber(ArrayList<Integer> list){
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		for(int i = 1; i<list.size(); i++){
			Integer avg = list.get(i) - list.get(i-1);
			list2.add(avg);
		}
		Integer all = 0;
		for(int i = 0; i<list2.size(); i++){
			all += list2.get(i);
		}
		//System.out.println("总数:"+ all);
		//System.out.println("个数:"+ list2.size());
		//System.out.println("平均:"+ (all / list2.size()+ 1));
		return all / list2.size();
	}
	
	
	
	
}
