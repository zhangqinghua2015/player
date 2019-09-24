package com.zqh.player.tools.common.util.transfer;


import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by orange on 18/2/28.
 */
public class ConvertUtils {
    public static String objectToString(Object o){
        if(o == null){
            return "";
        }else if(o instanceof String){
            return (String)o;
        }else if(o instanceof Integer){
            return String.valueOf((Integer)o);
        }else if(o instanceof Long){
            return String.valueOf((Long)o);
        }else if(o instanceof Double){
            return String.valueOf((Double)o);
        }else if(o instanceof Float){
            return String.valueOf((Float)o);
        }else if(o instanceof Boolean){
            return String.valueOf((Boolean)o);
        }else if(o instanceof Date){
            return DateUtils.getDate((Date)o,"yyyy-MM-dd HH:mm:ss");
        }
        else{
            return "";
        }
    }


    /**
     *将Object转换为Double 用于数字型计算
     */
    public static Double objectToDouble(Object o){
        if(o instanceof BigDecimal){
            return ((BigDecimal) o).doubleValue();
        }else if(o instanceof Integer){
            return Double.valueOf((Integer)o);
        }else if(o instanceof Double){
            return (Double)o;
        }else if(o instanceof Long){
            return ((Long) o).doubleValue();
        }else{
            return null;
        }
    }

    /**
     * 转义特殊字符
     * @param url
     * @return
     */
    public static String urlToString(String url){
        if(url==null) return null;
        try {

            url = url.replaceAll("%2C",",");

//            s = URLDecoder.decode(url,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 判断列表是否为空
     * @param list
     * @return
     */
    public static boolean isBlank(List<?> list){
        if(list==null||list.size()==0){
            return true;
        }
        return false;
    }

    /**
     * 字符串转列表
     * @param s
     * @param pattern
     * @return
     */
    public static List<String> strToList(String s,String pattern){

        if(StringUtils.isEmpty(s)){
            return null;
        }

        String[] sa = s.split(pattern);
        List<String> list = new ArrayList<String>();
        for(String s1:sa){
            list.add(s1);
        }
        return list;
    }

    /**
     * 字符串转列表
     * @param s
     * @param pattern
     * @return
     */
    public static List<String> strToNotNullList(String s,String pattern){

        if(StringUtils.isEmpty(s)){
            return Collections.EMPTY_LIST;
        }

        String[] sa = s.split(pattern);
        List<String> list = new ArrayList<String>();
        for(String s1:sa){
            list.add(s1);
        }
        return list;
    }


    /**
     * 字符串转列表
     * @param s
     * @param pattern
     * @return
     */
    public static List<Integer> strToIList(String s,String pattern){

        if(StringUtils.isEmpty(s)){
            return null;
        }

        String[] sa = s.split(pattern);
        List<Integer> list = new ArrayList<Integer>();
        for(String s1:sa){
            list.add(Integer.parseInt(s1));
        }
        return list;
    }

    /**
     * 字符串转列表
     * @param s
     * @param pattern
     * @return
     */
    public static Set<String> strToSet(String s, String pattern){

        if(StringUtils.isEmpty(s)){
            return null;
        }

        String[] sa = s.split(pattern);
        Set<String> set = new HashSet<String>();
        for(String s1:sa){
            set.add(s1);
        }
        return set;
    }

    /**
     * 列表转字符串
     * @param pattern
     * @return
     */
    public static String listToStr( List<String> list,String pattern){

        if(isBlank(list)){
            return "";
        }else{
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<list.size();i++){
                sb.append(list.get(i));
                sb.append(i<list.size()-1?pattern:"");
            }
            return sb.toString();
        }
    }

    public static List setToList(Set set){
        if(set == null || set.size()==0){
            return null;
        }
        List list = new ArrayList(set.size());
        list.addAll(set);
        return list;
    }

    /**
     * list 去重方法
     * @param list
     */
    public static void removeDuplicate(List list) {
        if (list == null || list.size() == 0)
            return;
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
    }

    public static int countListLen(List...lists){
        if(lists==null){
            return 0;
        }
        int sum = 0;
        for(List list : lists){
            if(list!=null){
                sum+=list.size();
            }
        }
        return sum;
    }

    public static String getNotNullValue(String value,String defaultValue){
        if(value==null){
            return defaultValue;
        }
        return value;
    }

    public static List addAll(List... lists){
        if(lists.length==0){
            return Collections.EMPTY_LIST;
        }else{
            List list = new ArrayList(100);
            for(List one : lists){
                if(!isBlank(one)){
                    list.addAll(one);
                }
            }
            return list;
        }
    }

    public static List<String> genJoinStrs(Collection c,String around){
        if(CollectionUtils.isEmpty(c))return Collections.EMPTY_LIST;
        List<String> list = new ArrayList<>(c.size());
        for(Object o : c){
            list.add(around+o.toString()+around);
        }
        return list;
    }

    public static Map listToMap(List list,String key){
        if(CollectionUtils.isEmpty(list))return Collections.EMPTY_MAP;
        Map map = new HashMap();
        try {
            Method method = list.get(0).getClass().getMethod("get"+key.substring(0,1).toUpperCase()+key.substring(1));
            for(Object obj : list){
                map.put(method.invoke(obj,new Class[]{}),obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_MAP;
        }
        return map;
    }

    public static  List getFieldList(List list,String key){
        if(CollectionUtils.isEmpty(list))return Collections.EMPTY_LIST;
        List keys = new ArrayList();
        try {
            Method method = list.get(0).getClass().getMethod("get"+key.substring(0,1).toUpperCase()+key.substring(1));
            for(Object obj : list){
                keys.add(method.invoke(obj,new Class[]{}));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return keys;
    }

    public static String nullToChar(String a){
        if(a==null)return "";
        return a;
    }

    public static Integer nullToIntOne(Integer a){
        if(a==null)return 1;
        return a;
    }

    public static Integer nullToIntZero(Integer a){
        if(a==null)return 0;
        return a;
    }

    public static Integer nullToIntZero(String a){
        if(a==null || a.trim().equals(""))return 0;
        return Integer.valueOf(a);
    }

    public static Double nullToDoubleZero(Double a){
        if(a==null)return 0d;
        return a;
    }

    public static Double nullToDoubleZero(String str){
        if(StringUtils.isEmpty(str))return 0D;
        return Double.valueOf(str);
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {

        if(obj == null) return null;
        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;
            map.put(key, value);
        }

        return map;

    }

    /**
     * 分割List
     *
     * @param list     待分割的list
     * @param pageSize 每段list的大小
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        List<List<T>> splittedList = new ArrayList<List<T>>();

        int listSize = list.size();
        if (listSize <= pageSize) {
            splittedList.add(list.subList(0, listSize));
        } else {
            int fromIndex = 0 - pageSize;
            int toIndex = 0;
            while (true) {
                fromIndex += pageSize;
                toIndex += pageSize;
                if (toIndex > listSize) {
                    toIndex = listSize;
                }
                splittedList.add(list.subList(fromIndex, toIndex));
                if (toIndex == listSize) {
                    break;
                }
            }
        }

        return splittedList;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
//        System.out.println(URLDecoder.decode("HangZhou LASION International Trading Co.%2CLtd","utf-8"));

        String json = "{\n" +
                "\t\"data\": {\n" +
                "\t\t\"total_products\": 13,\n" +
                "\t\t\"products\": [{\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Bronze\",\n" +
                "\t\t\t\t\"SellerSku\": \"it is a nice watch\",\n" +
                "\t\t\t\t\"ShopSku\": \"399890357_MY-577515211\",\n" +
                "\t\t\t\t\"watch_strap_color\": \"Bronze\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i399890357-s577515211.html\",\n" +
                "\t\t\t\t\"package_height\": \"3\",\n" +
                "\t\t\t\t\"price\": 123.0,\n" +
                "\t\t\t\t\"package_length\": \"11\",\n" +
                "\t\t\t\t\"seller_promotion\": \"212\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-07-26\",\n" +
                "\t\t\t\t\"Available\": 121,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-07-31\",\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 121,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/5a4a43ce8e80ef6874cebabfa517571f.jpg\", \"https://my-live-02.slatic.net/original/13c713ffa57036cdbe24815cdccaa03d.jpg\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"color_thumbnail\": \"https://my-live-02.slatic.net/original/da1144a60ff8eff3627b6ba2043c72f0.jpg\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"package_content\": \"it is just a product,you such ass\",\n" +
                "\t\t\t\t\"package_width\": \"11\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-07-31 00:00\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-07-26 00:00\",\n" +
                "\t\t\t\t\"special_price\": 121.0,\n" +
                "\t\t\t\t\"package_weight\": \"0.04\",\n" +
                "\t\t\t\t\"SkuId\": 577515211\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 399890357,\n" +
                "\t\t\t\"primary_category\": 8706,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"the very good hot fancy glass herui\",\n" +
                "\t\t\t\t\"short_description\": \"<ul>\\r\\n\\t<li>sadgas</li>\\r\\n\\t<li>dljljag</li>\\r\\n\\t<li>lajljga</li>\\r\\n\\t<li>glajdlsgj</li>\\r\\n\\t<li>lajslgj</li>\\r\\n\\t<li>ljalgj</li>\\r\\n\\t<li>lajslgjl</li>\\r\\n\\t<li>lajlgjljg</li>\\r\\n\\t<li>lasjlgjglj</li>\\r\\n\\t<li>ljalgjlgj</li>\\r\\n\\t<li>ljlgjljglj</li>\\r\\n\\t<li>lsjlgjlgjl</li>\\r\\n\\t<li>sjljglsjlsiduf</li>\\r\\n\\t<li>sldjgljsg</li>\\r\\n</ul>\\r\\n\",\n" +
                "\t\t\t\t\"description\": \"<div><img src=\\\"https://sg-test-11.slatic.net/shop/f55ef86cf7c2845932bd7a051dff2d7a.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/0b623dbc75973f60b17a3e66344c5231.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/128fa9d59dce8c9ae04533dc25b2be48.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/ee8087a8ad1831a519aedcd199e13067.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/16eba487a803a0b1155edb974a260b7b.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/5a134f04af058f3e75e9247b4211621a.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/8ea6275ff603555a71a64c6339394b4e.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/ba642ea737d763f559d904b0892d668f.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/b1de271437944e7c02de29b7f3e4d8f4.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/b27f944a4f368c126a0b0a4c7aef49bd.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/c75b02ac0f3d25539ac6e43761428186.jpeg\\\"/><img src=\\\"https://sg-test-11.slatic.net/shop/7b3d83a7c1df99356a046457f640dada.png\\\"/></div>\",\n" +
                "\t\t\t\t\"video\": \"http://swf.ws.126.net/openplayer/v01/-0-2_M8KE0J7VO_M8LMO4M56-vimg1_ws_126_net//image/snapshot_movie/2013/2/C/9/M8LMO58C9-1430711943278.swf\\\" type=\\\"application/x-shockwave-flash\\\" width=\\\"640\\\" height=\\\"360\\\" allowFullScreen=\\\"true\\\" wmode=\\\"transparent\\\" allowScri\",\n" +
                "\t\t\t\t\"brand\": \"No Brand\",\n" +
                "\t\t\t\t\"model\": \"12131\",\n" +
                "\t\t\t\t\"movement\": \"Automatic\",\n" +
                "\t\t\t\t\"glass\": \"Hardened Mineral Crystal Glass\",\n" +
                "\t\t\t\t\"watch_case_size\": \"25 to 29mm\",\n" +
                "\t\t\t\t\"feature\": \"Calculator,Altimeter,Chrono Active,Compass\",\n" +
                "\t\t\t\t\"movement_country\": \"Other\",\n" +
                "\t\t\t\t\"water_resistant\": \"300+ m\",\n" +
                "\t\t\t\t\"case_shape\": \"Retangle\",\n" +
                "\t\t\t\t\"strap\": \"Alloy\",\n" +
                "\t\t\t\t\"watch_dial_size\": \"26mm\",\n" +
                "\t\t\t\t\"color_family\": \"Blue Gray,Aqua,Blush Pink\",\n" +
                "\t\t\t\t\"warranty_type\": \"International Seller Warranty\",\n" +
                "\t\t\t\t\"warranty\": \"15 Years\",\n" +
                "\t\t\t\t\"product_warranty\": \"13123\",\n" +
                "\t\t\t\t\"name_ms\": \"jiuraljdlfjlj jfldsjlgill jlfdjlgjljdlj jsldgiuljsldjf\",\n" +
                "\t\t\t\t\"product_warranty_en\": \"djl ruoeu the sjif us english\",\n" +
                "\t\t\t\t\"Hazmat\": \"None\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 0,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Army Green EU:29\",\n" +
                "\t\t\t\t\"Images\": [\"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"121333\",\n" +
                "\t\t\t\t\"ShopSku\": \"361383947_MY-512703502\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i361383947-s512703502.html\",\n" +
                "\t\t\t\t\"package_width\": \"8\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-07-19 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Army Green\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-06-06 00:00\",\n" +
                "\t\t\t\t\"package_height\": \"3\",\n" +
                "\t\t\t\t\"size\": \"EU:29\",\n" +
                "\t\t\t\t\"special_price\": 100.0,\n" +
                "\t\t\t\t\"price\": 123.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-06-06\",\n" +
                "\t\t\t\t\"package_weight\": \"0.5\",\n" +
                "\t\t\t\t\"Available\": 0,\n" +
                "\t\t\t\t\"SkuId\": 512703502,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-07-19\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 361383947,\n" +
                "\t\t\t\"primary_category\": 4195,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"212121212\",\n" +
                "\t\t\t\t\"short_description\": \"<p>121212</p>\\r\\n\",\n" +
                "\t\t\t\t\"brand\": \"今麦郎 方便面 老爸厨房一菜一面 番茄鸡蛋浇头面 112g 碗装1碗  价格 RM8\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 111,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Army Green EU:24\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/9e0b8524bd956b52933d10a51a5a4efc.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"color_thumbnail\": \"https://my-live-01.slatic.net/original/0f22b07fb8932999b960bbe77f15ba6a.jpg\",\n" +
                "\t\t\t\t\"SellerSku\": \"12123-Army Green-24\",\n" +
                "\t\t\t\t\"ShopSku\": \"393217714_MY-567268131\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i393217714-s567268131.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-07-28 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Army Green\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-07-27 00:00\",\n" +
                "\t\t\t\t\"package_height\": \"12\",\n" +
                "\t\t\t\t\"size\": \"EU:24\",\n" +
                "\t\t\t\t\"special_price\": 11.0,\n" +
                "\t\t\t\t\"price\": 11.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-07-27\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 111,\n" +
                "\t\t\t\t\"SkuId\": 567268131,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-07-28\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 111,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Army Green EU:28\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/9e0b8524bd956b52933d10a51a5a4efc.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"color_thumbnail\": \"https://my-live-01.slatic.net/original/0f22b07fb8932999b960bbe77f15ba6a.jpg\",\n" +
                "\t\t\t\t\"SellerSku\": \"12123-Army Green-28\",\n" +
                "\t\t\t\t\"ShopSku\": \"393217714_MY-567268130\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i393217714-s567268130.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-07-28 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Army Green\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-07-27 00:00\",\n" +
                "\t\t\t\t\"package_height\": \"12\",\n" +
                "\t\t\t\t\"size\": \"EU:28\",\n" +
                "\t\t\t\t\"special_price\": 11.0,\n" +
                "\t\t\t\t\"price\": 11.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-07-27\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 111,\n" +
                "\t\t\t\t\"SkuId\": 567268130,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-07-28\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 111,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Apricot EU:24\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/2e7b61fbc85a7bac4555da2abe652bac.jpg\", \"https://my-live-02.slatic.net/original/5a4a43ce8e80ef6874cebabfa517571f.jpg\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"12123-Apricot-24\",\n" +
                "\t\t\t\t\"ShopSku\": \"393217714_MY-567268129\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i393217714-s567268129.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-07-28 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Apricot\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-07-27 00:00\",\n" +
                "\t\t\t\t\"package_height\": \"12\",\n" +
                "\t\t\t\t\"size\": \"EU:24\",\n" +
                "\t\t\t\t\"special_price\": 11.0,\n" +
                "\t\t\t\t\"price\": 11.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-07-27\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 111,\n" +
                "\t\t\t\t\"SkuId\": 567268129,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-07-28\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 111,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Apricot EU:28\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/2e7b61fbc85a7bac4555da2abe652bac.jpg\", \"https://my-live-02.slatic.net/original/5a4a43ce8e80ef6874cebabfa517571f.jpg\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"12123-Apricot-28\",\n" +
                "\t\t\t\t\"ShopSku\": \"393217714_MY-567268128\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i393217714-s567268128.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-07-28 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Apricot\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-07-27 00:00\",\n" +
                "\t\t\t\t\"package_height\": \"12\",\n" +
                "\t\t\t\t\"size\": \"EU:28\",\n" +
                "\t\t\t\t\"special_price\": 11.0,\n" +
                "\t\t\t\t\"price\": 11.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-07-27\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 111,\n" +
                "\t\t\t\t\"SkuId\": 567268128,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-07-28\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 393217714,\n" +
                "\t\t\t\"primary_category\": 1740,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"shawncharly\",\n" +
                "\t\t\t\t\"short_description\": \"<p></p>\\r\\n\\r\\n<ul>\\r\\n\\t<li>sadugouaga</li>\\r\\n\\t<li>dasjgaslg</li>\\r\\n</ul>\\r\\n\\r\\n<p style=\\\"text-align: center;\\\">gasdgasdg</p>\\r\\n\\r\\n<ul>\\r\\n\\t<li>gasgasd</li>\\r\\n</ul>\\r\\n\",\n" +
                "\t\t\t\t\"brand\": \"No Brand\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"inactive\",\n" +
                "\t\t\t\t\"quantity\": 20,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Aqua Int:12XL\",\n" +
                "\t\t\t\t\"Images\": [\"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"shawn-Aqua-12XL\",\n" +
                "\t\t\t\t\"ShopSku\": \"416520116_MY-602289025\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i416520116-s602289025.html\",\n" +
                "\t\t\t\t\"package_width\": \"11.0\",\n" +
                "\t\t\t\t\"special_to_time\": \"2101-12-31 23:45\",\n" +
                "\t\t\t\t\"color_family\": \"Aqua\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-08-14 09:45\",\n" +
                "\t\t\t\t\"package_height\": \"2.0\",\n" +
                "\t\t\t\t\"size\": \"Int:12XL\",\n" +
                "\t\t\t\t\"special_price\": 72.14,\n" +
                "\t\t\t\t\"price\": 74.7,\n" +
                "\t\t\t\t\"package_length\": \"2.0\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-08-14\",\n" +
                "\t\t\t\t\"package_weight\": \"0.05\",\n" +
                "\t\t\t\t\"Available\": 20,\n" +
                "\t\t\t\t\"SkuId\": 602289025,\n" +
                "\t\t\t\t\"special_to_date\": \"2101-12-31\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 416520116,\n" +
                "\t\t\t\"primary_category\": 1849,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"121213\",\n" +
                "\t\t\t\t\"short_description\": \"<ul>\\r\\n\\t<li>gasegasdgas</li>\\r\\n\\t<li>asd</li>\\r\\n\\t<li>gas</li>\\r\\n\\t<li>dg</li>\\r\\n\\t<li>asdgasd</li>\\r\\n\\t<li>gasdgas</li>\\r\\n\\t<li>dg</li>\\r\\n\\t<li>as</li>\\r\\n\\t<li>fg</li>\\r\\n</ul>\\r\\n\",\n" +
                "\t\t\t\t\"description\": \"<p>agasdjglasdg</p>\\r\\n\\r\\n<p>asd</p>\\r\\n\\r\\n<p>gas;jgasdjga</p>\\r\\n\\r\\n<p>asd;fas;kgas</p>\\r\\n\\r\\n<p></p>\\r\\n\",\n" +
                "\t\t\t\t\"brand\": \"No Brand\",\n" +
                "\t\t\t\t\"warranty_type\": \"No Warranty\",\n" +
                "\t\t\t\t\"name_ms\": \"121213\",\n" +
                "\t\t\t\t\"Hazmat\": \"None\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 20,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Army Green CN:100A\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/9e0b8524bd956b52933d10a51a5a4efc.jpg\", \"https://my-live-02.slatic.net/original/318746c72f27f9f11a068d24133978dd.jpg\", \"https://my-live-02.slatic.net/original/069dbf145068961c1a4fc71322484208.jpg\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"color_thumbnail\": \"https://my-live-02.slatic.net/original/d5a28ffb3f4ccbae3df8389a19cd5900.jpg\",\n" +
                "\t\t\t\t\"SellerSku\": \"123123-Antique White-CN:100B-Army Green-CN:100A\",\n" +
                "\t\t\t\t\"ShopSku\": \"392711439_MY-567185892\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i392711439-s567185892.html\",\n" +
                "\t\t\t\t\"package_width\": \"11.0\",\n" +
                "\t\t\t\t\"special_to_time\": \"2101-12-31 23:45\",\n" +
                "\t\t\t\t\"color_family\": \"Army Green\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-08-03 11:15\",\n" +
                "\t\t\t\t\"package_height\": \"11.0\",\n" +
                "\t\t\t\t\"size\": \"CN:100A\",\n" +
                "\t\t\t\t\"special_price\": 95.23,\n" +
                "\t\t\t\t\"price\": 102.73,\n" +
                "\t\t\t\t\"package_length\": \"11.0\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-08-03\",\n" +
                "\t\t\t\t\"package_weight\": \"1.0\",\n" +
                "\t\t\t\t\"Available\": 20,\n" +
                "\t\t\t\t\"SkuId\": 567185892,\n" +
                "\t\t\t\t\"special_to_date\": \"2101-12-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 20,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Antique White CN:100A\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/f72ce3c7b6fbae4da07fc78e2974764d.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"color_thumbnail\": \"https://my-live-02.slatic.net/original/966c040dcd1f8e637e56d1ad0ed4ab78.jpg\",\n" +
                "\t\t\t\t\"SellerSku\": \"123123-Antique White-CN:100B-Antique White-CN:100A\",\n" +
                "\t\t\t\t\"ShopSku\": \"392711439_MY-567185891\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i392711439-s567185891.html\",\n" +
                "\t\t\t\t\"package_width\": \"11.0\",\n" +
                "\t\t\t\t\"special_to_time\": \"2101-12-31 23:45\",\n" +
                "\t\t\t\t\"color_family\": \"Antique White\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-08-03 11:15\",\n" +
                "\t\t\t\t\"package_height\": \"11.0\",\n" +
                "\t\t\t\t\"size\": \"CN:100A\",\n" +
                "\t\t\t\t\"special_price\": 95.23,\n" +
                "\t\t\t\t\"price\": 102.73,\n" +
                "\t\t\t\t\"package_length\": \"11.0\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-08-03\",\n" +
                "\t\t\t\t\"package_weight\": \"1.0\",\n" +
                "\t\t\t\t\"Available\": 20,\n" +
                "\t\t\t\t\"SkuId\": 567185891,\n" +
                "\t\t\t\t\"special_to_date\": \"2101-12-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 20,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Army Green CN:100B\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/9e0b8524bd956b52933d10a51a5a4efc.jpg\", \"https://my-live-02.slatic.net/original/318746c72f27f9f11a068d24133978dd.jpg\", \"https://my-live-02.slatic.net/original/069dbf145068961c1a4fc71322484208.jpg\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"color_thumbnail\": \"https://my-live-02.slatic.net/original/d5a28ffb3f4ccbae3df8389a19cd5900.jpg\",\n" +
                "\t\t\t\t\"SellerSku\": \"123123-Army Green-CN:100B\",\n" +
                "\t\t\t\t\"ShopSku\": \"392711439_MY-566493707\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i392711439-s566493707.html\",\n" +
                "\t\t\t\t\"package_width\": \"11.0\",\n" +
                "\t\t\t\t\"special_to_time\": \"2101-12-31 23:45\",\n" +
                "\t\t\t\t\"color_family\": \"Army Green\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-08-03 11:15\",\n" +
                "\t\t\t\t\"package_height\": \"11.0\",\n" +
                "\t\t\t\t\"size\": \"CN:100B\",\n" +
                "\t\t\t\t\"special_price\": 95.23,\n" +
                "\t\t\t\t\"price\": 102.73,\n" +
                "\t\t\t\t\"package_length\": \"11.0\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-08-03\",\n" +
                "\t\t\t\t\"package_weight\": \"1.0\",\n" +
                "\t\t\t\t\"Available\": 20,\n" +
                "\t\t\t\t\"SkuId\": 566493707,\n" +
                "\t\t\t\t\"special_to_date\": \"2101-12-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 30,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Antique White CN:100B\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/f72ce3c7b6fbae4da07fc78e2974764d.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"color_thumbnail\": \"https://my-live-02.slatic.net/original/966c040dcd1f8e637e56d1ad0ed4ab78.jpg\",\n" +
                "\t\t\t\t\"SellerSku\": \"123123-Antique White-CN:100B\",\n" +
                "\t\t\t\t\"ShopSku\": \"392711439_MY-566493706\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i392711439-s566493706.html\",\n" +
                "\t\t\t\t\"package_width\": \"11.0\",\n" +
                "\t\t\t\t\"special_to_time\": \"2101-12-31 23:45\",\n" +
                "\t\t\t\t\"color_family\": \"Antique White\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-08-03 11:15\",\n" +
                "\t\t\t\t\"package_height\": \"11.0\",\n" +
                "\t\t\t\t\"size\": \"CN:100B\",\n" +
                "\t\t\t\t\"special_price\": 95.23,\n" +
                "\t\t\t\t\"price\": 102.73,\n" +
                "\t\t\t\t\"package_length\": \"11.0\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-08-03\",\n" +
                "\t\t\t\t\"package_weight\": \"1.0\",\n" +
                "\t\t\t\t\"Available\": 30,\n" +
                "\t\t\t\t\"SkuId\": 566493706,\n" +
                "\t\t\t\t\"special_to_date\": \"2101-12-31\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 392711439,\n" +
                "\t\t\t\"primary_category\": 1740,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"12121\",\n" +
                "\t\t\t\t\"short_description\": \"<p>1212121212</p>\\r\\n\\r\\n<p>1331212</p>\\r\\n\\r\\n<p>12345</p>\\r\\n\\r\\n<p>fasdf</p>\\r\\n\\r\\n<p>dsga</p>\\r\\n\\r\\n<p></p>\\r\\n\",\n" +
                "\t\t\t\t\"description\": \"<p>asdgasgasdg</p>\\r\\n\\r\\n<p><img alt=\\\"蜘蛛侠1.jpg\\\" src=\\\"https://my-live-02.slatic.net/original/2e7b61fbc85a7bac4555da2abe652bac.jpg\\\"/></p>\\r\\n\",\n" +
                "\t\t\t\t\"brand\": \"12 Benefits\",\n" +
                "\t\t\t\t\"warranty_type\": \"No Warranty\",\n" +
                "\t\t\t\t\"name_ms\": \"12121\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 1,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"White Int:XXL\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/f14a3c1fff3d1ca8fd84b14dfb0dff95.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-WXXL\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612342\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612342.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"White\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:45\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:XXL\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 1,\n" +
                "\t\t\t\t\"SkuId\": 426612342,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"White Int:XL\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live.slatic.net/original/a7096882eda339ddfa8a70dff61c2cfe.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-WXL\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612341\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612341.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"White\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:45\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:XL\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612341,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"White Int:L\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/a7096882eda339ddfa8a70dff61c2cfe.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-WL\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612340\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612340.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"White\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:45\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:L\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612340,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"White Int:M\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/f14a3c1fff3d1ca8fd84b14dfb0dff95.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-WM\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612339\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612339.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"White\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:45\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:M\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612339,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"White Int:S\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live.slatic.net/original/a7096882eda339ddfa8a70dff61c2cfe.jpg\", \"https://my-live.slatic.net/original/5afb814e6401f6da603bc874b2783ee2.jpg\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-WS\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612338\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612338.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"White\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:30\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:S\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612338,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black Int:XXL\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/2d9a9a3e1c7a0f77b177419b7dacf1a6.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-BXXL\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612337\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612337.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Black\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:30\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:XXL\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612337,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black Int:XL\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live.slatic.net/original/2d9a9a3e1c7a0f77b177419b7dacf1a6.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-BXL\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612336\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612336.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Black\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:30\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:XL\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612336,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black Int:L\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live.slatic.net/original/2d9a9a3e1c7a0f77b177419b7dacf1a6.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-BL\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612335\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612335.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Black\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:30\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:L\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612335,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black Int:M\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/a7096882eda339ddfa8a70dff61c2cfe.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-BM\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612334\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612334.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Black\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:30\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:M\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612334,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black Int:S\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/b9f6f8476a0fcc6dbb7e56d9d2b33340.jpg\", \"https://my-live.slatic.net/original/5afb814e6401f6da603bc874b2783ee2.jpg\", \"https://my-live.slatic.net/original/2d9a9a3e1c7a0f77b177419b7dacf1a6.jpg\", \"https://my-live-02.slatic.net/original/d3e7c7dca8f19f5d508235fee1adbefb.jpg\", \"https://my-live.slatic.net/original/a7096882eda339ddfa8a70dff61c2cfe.jpg\", \"https://my-live.slatic.net/original/24cf370e2d3001d94ac8112abbe959b6.jpg\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"test0001-BS\",\n" +
                "\t\t\t\t\"ShopSku\": \"312185684_MY-426612333\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i312185684-s426612333.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-03-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Black\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-03-22 15:30\",\n" +
                "\t\t\t\t\"package_height\": \"10\",\n" +
                "\t\t\t\t\"size\": \"Int:S\",\n" +
                "\t\t\t\t\"special_price\": 30.0,\n" +
                "\t\t\t\t\"price\": 100.0,\n" +
                "\t\t\t\t\"package_length\": \"10\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-03-22\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 426612333,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-03-31\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 312185684,\n" +
                "\t\t\t\"primary_category\": 4195,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"Men Absorbant Underwear Body Shaper Belly Cincher Waist Tight Lose Weight GL\",\n" +
                "\t\t\t\t\"short_description\": \"<p>&quot;Material: 80% nylon, 20% Spandex. ? With incredible men&#39;s slimming shirt, It is a revolutionary slimming and lifting undergarment. ? Can give you a sexy, curvy hour glass figure instantly. ? Uniquely designed firming panels to trim and tighten that bulging stomach. ? It will help you fit into your favorite pair of jeans or tailored business suits comfortably. ? Instant Abs instead of that beer belly. package&pound;&ordm;1* corset &nbsp;shapewear&pound;&uml;without retail box&pound;&copy;. Size &nbsp;length Bust(Flat) Bust (Stretched) S 58/23&quot;&quot; 52/21&quot;&quot; 90/36&quot;&quot; M 62/25&quot;&quot; 64/25&quot;&quot; 104/41&quot;&quot; L 62/25&quot;&quot; 75/30&quot;&quot; 120/47.3&quot;&quot; XL 64/25&quot;&quot; 84/33&quot;&quot; 134/53&quot;&quot; &nbsp;2XL 63/25&quot;&quot; 100/39&quot;&quot; 156/62&quot;&quot;.&quot;</p>\\n\",\n" +
                "\t\t\t\t\"brand\": \"ENO\",\n" +
                "\t\t\t\t\"warranty_type\": \"No Warranty\",\n" +
                "\t\t\t\t\"warranty\": \"1 Month\",\n" +
                "\t\t\t\t\"name_ms\": \"Men Absorbant Underwear Body Shaper Belly Cincher Waist Tight Lose Weight GL\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 0,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black CM:100\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/7e002ac412a17789fad7e8a450215810.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"black-100\",\n" +
                "\t\t\t\t\"ShopSku\": \"358946485_MY-508508969\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i358946485-s508508969.html\",\n" +
                "\t\t\t\t\"package_width\": \"16\",\n" +
                "\t\t\t\t\"color_family\": \"Black\",\n" +
                "\t\t\t\t\"package_height\": \"16\",\n" +
                "\t\t\t\t\"size\": \"CM:100\",\n" +
                "\t\t\t\t\"special_price\": 0.0,\n" +
                "\t\t\t\t\"price\": 109.0,\n" +
                "\t\t\t\t\"package_length\": \"16\",\n" +
                "\t\t\t\t\"package_weight\": \"0.5\",\n" +
                "\t\t\t\t\"Available\": 0,\n" +
                "\t\t\t\t\"SkuId\": 508508969\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 358946485,\n" +
                "\t\t\t\"primary_category\": 11382,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"T-shirt\",\n" +
                "\t\t\t\t\"short_description\": \"<ul>\\r\\n\\t<li>very nice.</li>\\r\\n\\t<li>so good.</li>\\r\\n\\t<li>so soft.</li>\\r\\n</ul>\\r\\n\",\n" +
                "\t\t\t\t\"description\": \"<p>very nice, so great so good.</p>\\r\\n\",\n" +
                "\t\t\t\t\"brand\": \"OEM\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"inactive\",\n" +
                "\t\t\t\t\"quantity\": 25,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black HEIGHT:180CM\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/2e8cff34b226d3f5e22c16795155caa7.jpg\", \"https://my-live-02.slatic.net/original/aada6168d0620b855fc9d43554d8a559.jpg\", \"https://my-live-02.slatic.net/original/26c099e50351b255bf92927bbdd5eedf.jpg\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"good t-shirt-Black-HEIGHT:180CM\",\n" +
                "\t\t\t\t\"ShopSku\": \"397732416_MY-574868072\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i397732416-s574868072.html\",\n" +
                "\t\t\t\t\"package_width\": \"20.0\",\n" +
                "\t\t\t\t\"special_to_time\": \"2101-12-31 23:45\",\n" +
                "\t\t\t\t\"color_family\": \"Black\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-08-10 18:30\",\n" +
                "\t\t\t\t\"package_height\": \"2.0\",\n" +
                "\t\t\t\t\"size\": \"HEIGHT:180CM\",\n" +
                "\t\t\t\t\"special_price\": 138.89,\n" +
                "\t\t\t\t\"price\": 145.71,\n" +
                "\t\t\t\t\"package_length\": \"20.0\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-08-10\",\n" +
                "\t\t\t\t\"package_weight\": \"0.08\",\n" +
                "\t\t\t\t\"Available\": 25,\n" +
                "\t\t\t\t\"SkuId\": 574868072,\n" +
                "\t\t\t\t\"special_to_date\": \"2101-12-31\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 397732416,\n" +
                "\t\t\t\"primary_category\": 4195,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"cloth good very good nice\",\n" +
                "\t\t\t\t\"short_description\": \"<ul>\\r\\n\\t<li style=\\\"margin-left: 120.0px;\\\">cheap</li>\\r\\n\\t<li style=\\\"margin-left: 120.0px;\\\">good</li>\\r\\n\\t<li style=\\\"margin-left: 120.0px;\\\">sex</li>\\r\\n\\t<li style=\\\"margin-left: 120.0px;\\\">man</li>\\r\\n\\t<li style=\\\"margin-left: 120.0px;\\\">hot</li>\\r\\n</ul>\\r\\n\",\n" +
                "\t\t\t\t\"description\": \"<p><img alt=\\\"hanes-mens-5-2-oz-heavyweight-short-sleeve-t-shirt-5280-black_-_5100_5280_black.jpg\\\" src=\\\"https://my-live-02.slatic.net/original/2e8cff34b226d3f5e22c16795155caa7.jpg\\\" style=\\\"width: 1005.0px;height: 1005.0px;\\\"/></p>\\r\\n\\r\\n<p>very good you can't miss it</p>\\r\\n\\r\\n<p>every man should have one</p>\\r\\n\",\n" +
                "\t\t\t\t\"brand\": \"5-niu\",\n" +
                "\t\t\t\t\"model\": \"s-101\",\n" +
                "\t\t\t\t\"sleeves\": \"Short Sleeve\",\n" +
                "\t\t\t\t\"tee_sleeve_length\": \"Short\",\n" +
                "\t\t\t\t\"warranty_type\": \"No Warranty\",\n" +
                "\t\t\t\t\"name_ms\": \"cloth good very good nice\",\n" +
                "\t\t\t\t\"Hazmat\": \"None\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"inactive\",\n" +
                "\t\t\t\t\"quantity\": 12,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Army Green CN:70A\",\n" +
                "\t\t\t\t\"Images\": [\"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"hotshawngods-Army Green-70A\",\n" +
                "\t\t\t\t\"ShopSku\": \"409544188_MY-593207169\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i409544188-s593207169.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"color_family\": \"Army Green\",\n" +
                "\t\t\t\t\"package_height\": \"5\",\n" +
                "\t\t\t\t\"size\": \"CN:70A\",\n" +
                "\t\t\t\t\"special_price\": 0.0,\n" +
                "\t\t\t\t\"price\": 133.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"package_weight\": \"0.09\",\n" +
                "\t\t\t\t\"Available\": 12,\n" +
                "\t\t\t\t\"SkuId\": 593207169\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"inactive\",\n" +
                "\t\t\t\t\"quantity\": 12,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Avocado CN:70A\",\n" +
                "\t\t\t\t\"Images\": [\"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"hotshawngods-Avocado-70A\",\n" +
                "\t\t\t\t\"ShopSku\": \"409544188_MY-593207168\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i409544188-s593207168.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"color_family\": \"Avocado\",\n" +
                "\t\t\t\t\"package_height\": \"5\",\n" +
                "\t\t\t\t\"size\": \"CN:70A\",\n" +
                "\t\t\t\t\"special_price\": 0.0,\n" +
                "\t\t\t\t\"price\": 133.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"package_weight\": \"0.09\",\n" +
                "\t\t\t\t\"Available\": 12,\n" +
                "\t\t\t\t\"SkuId\": 593207168\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"inactive\",\n" +
                "\t\t\t\t\"quantity\": 12,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Army Green CN:110C\",\n" +
                "\t\t\t\t\"Images\": [\"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"hotshawngods-Army Green-110C\",\n" +
                "\t\t\t\t\"ShopSku\": \"409544188_MY-593207167\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i409544188-s593207167.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"color_family\": \"Army Green\",\n" +
                "\t\t\t\t\"package_height\": \"5\",\n" +
                "\t\t\t\t\"size\": \"CN:110C\",\n" +
                "\t\t\t\t\"special_price\": 0.0,\n" +
                "\t\t\t\t\"price\": 133.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"package_weight\": \"0.09\",\n" +
                "\t\t\t\t\"Available\": 12,\n" +
                "\t\t\t\t\"SkuId\": 593207167\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Status\": \"inactive\",\n" +
                "\t\t\t\t\"quantity\": 12,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Avocado CN:110C\",\n" +
                "\t\t\t\t\"Images\": [\"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"hotshawngods-Avocado-110C\",\n" +
                "\t\t\t\t\"ShopSku\": \"409544188_MY-593207166\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i409544188-s593207166.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"color_family\": \"Avocado\",\n" +
                "\t\t\t\t\"package_height\": \"5\",\n" +
                "\t\t\t\t\"size\": \"CN:110C\",\n" +
                "\t\t\t\t\"special_price\": 0.0,\n" +
                "\t\t\t\t\"price\": 133.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"package_weight\": \"0.09\",\n" +
                "\t\t\t\t\"Available\": 12,\n" +
                "\t\t\t\t\"SkuId\": 593207166\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 409544188,\n" +
                "\t\t\t\"primary_category\": 1740,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"sex hot very ggod\",\n" +
                "\t\t\t\t\"short_description\": \"<ul>\\r\\n\\t<li>fadsgasdf</li>\\r\\n\\t<li>ga</li>\\r\\n\\t<li>sdg</li>\\r\\n\\t<li>a'</li>\\r\\n</ul>\\r\\n\",\n" +
                "\t\t\t\t\"brand\": \"No Brand\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 18,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/da1144a60ff8eff3627b6ba2043c72f0.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"watchNo1\",\n" +
                "\t\t\t\t\"ShopSku\": \"399849923_MY-577463065\",\n" +
                "\t\t\t\t\"watch_strap_color\": \"Black\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"package_content\": \"watch\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i399849923-s577463065.html\",\n" +
                "\t\t\t\t\"package_width\": \"12.0\",\n" +
                "\t\t\t\t\"special_to_time\": \"2101-12-31 23:45\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-08-13 18:15\",\n" +
                "\t\t\t\t\"package_height\": \"3.0\",\n" +
                "\t\t\t\t\"special_price\": 68.88,\n" +
                "\t\t\t\t\"price\": 75.7,\n" +
                "\t\t\t\t\"package_length\": \"11.0\",\n" +
                "\t\t\t\t\"seller_promotion\": \"i have not got yet\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-08-13\",\n" +
                "\t\t\t\t\"package_weight\": \"0.04\",\n" +
                "\t\t\t\t\"Available\": 18,\n" +
                "\t\t\t\t\"SkuId\": 577463065,\n" +
                "\t\t\t\t\"special_to_date\": \"2101-12-31\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 399849923,\n" +
                "\t\t\t\"primary_category\": 8706,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"the very good hot fancy glass herui\",\n" +
                "\t\t\t\t\"short_description\": \"<ul>\\r\\n\\t<li>1jiuloul12</li>\\r\\n\\t<li><a>djgdspdofk;k</a></li>\\r\\n\\t<li><em>sdgajljsadg</em></li>\\r\\n\\t<li><strong><em><u>gasdgasdg</u></em></strong></li>\\r\\n\\t<li>\\r\\n\\t<h1><strong><em><u>gajlsjdglasd</u></em></strong></h1>\\r\\n\\t</li>\\r\\n</ul>\\r\\n\",\n" +
                "\t\t\t\t\"description\": \"<p>12sdagdsgsadfsdljlgjdsg</p>\\r\\n\\r\\n<p>lfdjslgjlasldjgadsgadsgasd;l</p>\\r\\n\\r\\n<p>ldsjgljasdjlgasd;jgadsg</p>\\r\\n\\r\\n<p><img src=\\\"https://my-live-02.slatic.net/original/3a922fd4e95962f127eb827c027b21e0.jpg\\\" style=\\\"width: 474.0px;height: 355.0px;\\\"/></p>\\r\\n\",\n" +
                "\t\t\t\t\"video\": \"http://swf.ws.126.net/openplayer/v01/-0-2_MA32UFQ0G_MA338VSK1-vimg1_ws_126_net\",\n" +
                "\t\t\t\t\"brand\": \"JIU JIU AI\",\n" +
                "\t\t\t\t\"model\": \"jiu22\",\n" +
                "\t\t\t\t\"movement\": \"Mechanical\",\n" +
                "\t\t\t\t\"glass\": \"Hardlex Crystal\",\n" +
                "\t\t\t\t\"watch_case_size\": \"45mm to 49mm\",\n" +
                "\t\t\t\t\"feature\": \"Dual Time,Interchangeable,Depth Measurement,Power Reserve,Chronograph\",\n" +
                "\t\t\t\t\"movement_country\": \"Japan\",\n" +
                "\t\t\t\t\"water_resistant\": \"150m\",\n" +
                "\t\t\t\t\"case_shape\": \"Short Sleeve\",\n" +
                "\t\t\t\t\"strap\": \"Ceramic\",\n" +
                "\t\t\t\t\"watch_dial_size\": \"26mm\",\n" +
                "\t\t\t\t\"color_family\": \"Beige,Blue,Avocado,Apricot,Army Green\",\n" +
                "\t\t\t\t\"warranty_type\": \"No Warranty\",\n" +
                "\t\t\t\t\"name_ms\": \"the very good hot fancy glass herui\",\n" +
                "\t\t\t\t\"product_warranty_en\": \"as i can say that is very good for us to speak english you are bis sb\",\n" +
                "\t\t\t\t\"Hazmat\": \"None\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"inactive\",\n" +
                "\t\t\t\t\"quantity\": 122,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Black\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/9e0b8524bd956b52933d10a51a5a4efc.jpg\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"1213\",\n" +
                "\t\t\t\t\"ShopSku\": \"396609243_MY-572694603\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i396609243-s572694603.html\",\n" +
                "\t\t\t\t\"package_width\": \"11\",\n" +
                "\t\t\t\t\"color_family\": \"Black\",\n" +
                "\t\t\t\t\"package_height\": \"11\",\n" +
                "\t\t\t\t\"special_price\": 0.0,\n" +
                "\t\t\t\t\"price\": 11.0,\n" +
                "\t\t\t\t\"package_length\": \"11\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 122,\n" +
                "\t\t\t\t\"SkuId\": 572694603\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 396609243,\n" +
                "\t\t\t\"primary_category\": 8932,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"12312312\",\n" +
                "\t\t\t\t\"short_description\": \"<ul>\\r\\n\\t<li style=\\\"text-align: center;\\\">adsga</li>\\r\\n\\t<li style=\\\"text-align: center;\\\">dfadsgasdgk\\r\\n\\t<ol>\\r\\n\\t\\t<li style=\\\"text-align: center;\\\">dagadsg</li>\\r\\n\\t\\t<li style=\\\"text-align: center;\\\">dgajg</li>\\r\\n\\t</ol>\\r\\n\\t</li>\\r\\n\\t<li style=\\\"text-align: center;\\\"><img alt=\\\"16_43_44__07_19_2018.jpg\\\" src=\\\"https://my-live-02.slatic.net/original/fa7e788514121128dc4be1fce9909acb.jpg\\\" style=\\\"width: 852.0px;height: 640.0px;\\\"/></li>\\r\\n</ul>\\r\\n\",\n" +
                "\t\t\t\t\"brand\": \"No Brand\",\n" +
                "\t\t\t\t\"model\": \"12w\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"active\",\n" +
                "\t\t\t\t\"quantity\": 122,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"Apricot CN:70A\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/b96724c7d690f0207a9ef8f684e3c3c7.jpg\", \"https://my-live-02.slatic.net/original/88c7d93b346cb2fc934ded04e6687e71.jpg\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"SellerSku\": \"123124\",\n" +
                "\t\t\t\t\"ShopSku\": \"386870931_MY-557071914\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i386870931-s557071914.html\",\n" +
                "\t\t\t\t\"package_width\": \"12\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-07-31 00:00\",\n" +
                "\t\t\t\t\"color_family\": \"Apricot\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-07-20 00:00\",\n" +
                "\t\t\t\t\"package_height\": \"12\",\n" +
                "\t\t\t\t\"size\": \"CN:70A\",\n" +
                "\t\t\t\t\"special_price\": 12.0,\n" +
                "\t\t\t\t\"price\": 12.0,\n" +
                "\t\t\t\t\"package_length\": \"12\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-07-20\",\n" +
                "\t\t\t\t\"package_weight\": \"1\",\n" +
                "\t\t\t\t\"Available\": 122,\n" +
                "\t\t\t\t\"SkuId\": 557071914,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-07-31\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 386870931,\n" +
                "\t\t\t\"primary_category\": 11322,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"fancy clothes\",\n" +
                "\t\t\t\t\"short_description\": \"<h2>good goods come on</h2>\\r\\n\\r\\n<h2>gadsgjasd</h2>\\r\\n\\r\\n<h2>gasgjas</h2>\\r\\n\",\n" +
                "\t\t\t\t\"description\": \"<div><img src=\\\"https://my-test-11.slatic.net/shop/21c6adc4d22b76b3aea5711c0a5c045b.jpeg\\\"/><img src=\\\"https://my-live-02.slatic.net/original/ea5661a6f751d8fea3485b56dcf28c69.jpg\\\"/><img src=\\\"https://my-live-02.slatic.net/original/1cce31e0e3892093cdb61783acaf0b94.jpg\\\"/><img src=\\\"https://my-live-02.slatic.net/original/8a33b918a8dc972cae61725763966401.jpg\\\"/><img src=\\\"https://my-live-02.slatic.net/original/ebb29207fd060536bef7d7166e02bcbc.jpg\\\"/></div>\",\n" +
                "\t\t\t\t\"brand\": \"No Brand\"\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"skus\": [{\n" +
                "\t\t\t\t\"Status\": \"inactive\",\n" +
                "\t\t\t\t\"quantity\": 10,\n" +
                "\t\t\t\t\"tax_class\": \"default\",\n" +
                "\t\t\t\t\"_compatible_variation_\": \"red\",\n" +
                "\t\t\t\t\"Images\": [\"https://my-live-02.slatic.net/original/a514126abd808fb400e98adfbeb0696e.jpg\", \"https://my-live-02.slatic.net/original/78a6ab38a7e4715625b92cb622be7f4a.jpg\", \"\", \"\", \"\", \"\", \"\", \"\"],\n" +
                "\t\t\t\t\"color_thumbnail\": \"https://my-live-02.slatic.net/original/a514126abd808fb400e98adfbeb0696e.jpg\",\n" +
                "\t\t\t\t\"SellerSku\": \"sku1\",\n" +
                "\t\t\t\t\"ShopSku\": \"422347699_MY-615183506\",\n" +
                "\t\t\t\t\"special_time_format\": \"yyyy-MM-dd HH:mm\",\n" +
                "\t\t\t\t\"package_content\": \"测试\",\n" +
                "\t\t\t\t\"Url\": \"https://www.lazada.com.my/-i422347699-s615183506.html\",\n" +
                "\t\t\t\t\"package_width\": \"10\",\n" +
                "\t\t\t\t\"special_to_time\": \"2018-08-30 19:15\",\n" +
                "\t\t\t\t\"color_family\": \"red\",\n" +
                "\t\t\t\t\"special_from_time\": \"2018-08-30 19:15\",\n" +
                "\t\t\t\t\"package_height\": \"20\",\n" +
                "\t\t\t\t\"special_price\": 5.0,\n" +
                "\t\t\t\t\"price\": 5.0,\n" +
                "\t\t\t\t\"package_length\": \"20\",\n" +
                "\t\t\t\t\"special_from_date\": \"2018-08-30\",\n" +
                "\t\t\t\t\"package_weight\": \"10\",\n" +
                "\t\t\t\t\"Available\": 10,\n" +
                "\t\t\t\t\"SkuId\": 615183506,\n" +
                "\t\t\t\t\"special_to_date\": \"2018-08-30\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"item_id\": 422347699,\n" +
                "\t\t\t\"primary_category\": 10001963,\n" +
                "\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\"name\": \"test\",\n" +
                "\t\t\t\t\"short_description\": \"<p>highligt1233445</p>\",\n" +
                "\t\t\t\t\"description\": \"<p>xxxx55555xxxxxx</p>\",\n" +
                "\t\t\t\t\"brand\": \"2K Games\",\n" +
                "\t\t\t\t\"model\": \"S\",\n" +
                "\t\t\t\t\"Hazmat\": \"Battery,Flammable\"\n" +
                "\t\t\t}\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"code\": \"0\",\n" +
                "\t\"request_id\": \"0ba9f84315361492288555330\"\n" +
                "}";
        Object obj = JSON.parseObject(json, Object.class);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            System.out.println(1);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
