package com.myspring.v2.servlet;

import com.myspring.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @ClassName MyDispatcherServlet
 * @Author LIUHANPENG
 * @Date 2020/1/15 0015 14:27
 **/
public class MyDispatcherServlet extends HttpServlet {

    private Properties configContext = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String,Object> ioc = new HashMap<>();

    private Map<String,Method> handlerMapping = new HashMap<>();

    private Map<String,Object> mapping = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req,resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception: "+ Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String url = req.getRequestURI();
           String contextPath = req.getContextPath();

        url = url.replace(contextPath,"").replaceAll("/+","/");
        if(!this.handlerMapping.containsKey(url)){
            resp.getWriter().write("404 NOT FOUND !");
            return;
        }

        Method method = (Method) this.handlerMapping.get(url);
        Map<String,String[]> params = req.getParameterMap();

        Class<?>[] parameterTypes = method.getParameterTypes();

        Object[] paramValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class parameterType = parameterTypes[i];
            if(parameterType == HttpServletRequest.class){
                paramValues[i] = req;
            }else if(parameterType == HttpServletResponse.class){
                paramValues[i]= resp;
            }else if(parameterType ==String.class){
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (int j = 0; j < parameterAnnotations.length; j++) {
                    for (Annotation a:parameterAnnotations[j]) {
                        if(a instanceof MyRequestParam){
                            String paramName = ((MyRequestParam) a).value();
                            if(!"".equals(paramName.trim())){
                                String value = Arrays.toString(params.get(paramName)).replaceAll("\\[|\\]","")
                                        .replaceAll("\\s",",");
                                paramValues[j]=value;
                            }
                        }
                    }
                }
            }else if(parameterType == int.class || parameterType == Integer.class){
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (int j = 0; j < parameterAnnotations.length; j++) {
                    for (Annotation a:parameterAnnotations[j]) {
                        if(a instanceof MyRequestParam){
                            String paramName = ((MyRequestParam) a).value();
                            if(!"".equals(paramName.trim())){
                                String value = Arrays.toString(params.get(paramName)).replaceAll("\\[|\\]","")
                                        .replaceAll("\\s",",");
                                paramValues[j]=Integer.valueOf(value);
                            }
                        }
                    }
                }
            }
        }
        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        method.invoke(ioc.get(beanName),paramValues);
    }

    @Override
    public void init(ServletConfig config){
        //1 加载配置
        doLoadConfig(config,config.getInitParameter("contextConfigLocation"));
        //2 扫描相关类
        doScanner(configContext.getProperty("scanPackage"));
        //3 初始化扫描的类，放入IOC容器
        doInstance();
        //4 依赖注入
        doAutuwired();
        //5 初始化handerMapping
        initHandlerMapping();
        System.out.println("My MVC Framework is init");
    }

    /**|
     *
     */
    private void initHandlerMapping() {
        if(ioc.isEmpty()){
            return;
        }
        for (Map.Entry<String,Object> entry: ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if(!clazz.isAnnotationPresent(MyController.class)){
                continue;
            }

            String baseUrl="";
            if(clazz.isAnnotationPresent(MyRequestMapping.class)){
                MyRequestMapping re = clazz.getAnnotation(MyRequestMapping.class);
                baseUrl = re.value();
            }

            for(Method method:clazz.getMethods()){
                if(!method.isAnnotationPresent(MyRequestMapping.class)){
                    continue;
                }
                MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);
                String url = ("/"+baseUrl+"/"+requestMapping.value()).replaceAll("/+","/");
                handlerMapping.put(url,method);
                System.out.println("Mapped :"+url+","+method);
            }
        }
    }

    /**
     * 依赖
     */
    private void doAutuwired() {
        if(ioc.isEmpty()){
            return;
        }
        for(Map.Entry<String,Object> entry:ioc.entrySet()){
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for(Field field:fields){
                if(!field.isAnnotationPresent(MyAutowired.class)){
                    continue;
                }
                MyAutowired annotation = field.getAnnotation(MyAutowired.class);
                String beanName = annotation.value().trim();
                if("".equals(beanName)){
                    beanName = toLowerFirstCase(field.getType().getSimpleName());
                }

                field.setAccessible(true);

                try {
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化ioc
     */
    private void doInstance() {
        if(classNames.isEmpty()){
            return;
        }
        try {
            for (String className:classNames) {
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(MyController.class)){
                    Object instance = clazz.newInstance();
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    ioc.put(beanName,instance);
                }else if(clazz.isAnnotationPresent(MyService.class)){
                    MyService service = clazz.getAnnotation(MyService.class);
                    String beanName = service.value();
                    if("".endsWith(beanName)){
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName,instance);
                    for (Class<?> i: clazz.getInterfaces()) {
                        if(ioc.containsKey(i.getName())){
                            throw new Exception("The "+i.getName()+" is exists!");
                        }
                        ioc.put(toLowerFirstCase(i.getSimpleName()),instance);
                    }
                }else{
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 首字母小写
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }

    /**
     * 加载配置
     * @param config
     * @param contextConfigLocation
     */
    private void doLoadConfig(ServletConfig config,String contextConfigLocation) {
        InputStream is= null;
        try {
            ServletContext servletContext = config.getServletContext();
             is = servletContext.getResourceAsStream(contextConfigLocation);
            configContext.load(is);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 扫描
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        for(File file:classDir.listFiles()){
            if(file.isDirectory()){
                doScanner(scanPackage+"."+file.getName());
            }else{
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                String clazzName = (scanPackage+"."+file.getName().replace(".class",""));
                classNames.add(clazzName);
            }
        }
    }
}
