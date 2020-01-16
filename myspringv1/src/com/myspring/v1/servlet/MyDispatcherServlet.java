package com.myspring.v1.servlet;

import com.myspring.annotation.MyAutowired;
import com.myspring.annotation.MyController;
import com.myspring.annotation.MyRequestMapping;
import com.myspring.annotation.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        if(!this.mapping.containsKey(url)){
            resp.getWriter().write("404 NOT FOUND !");
            return;
        }

        Method method = (Method) this.mapping.get(url);
        Map<String,String[]> params = req.getParameterMap();
        method.invoke(this.mapping.get(method.getDeclaringClass().getName()),new Object[]{req,resp,params.get("name")[0]});
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        Properties configContext = new Properties();
        InputStream is= null;
        try {
            String contextConfigLocation = config.getInitParameter("contextConfigLocation");
            ServletContext servletContext = config.getServletContext();
            is = servletContext.getResourceAsStream(contextConfigLocation);
            configContext.load(is);
            String scanPackage = configContext.getProperty("scanPackage");
            doScanner(scanPackage);
            Map<String,Object> localMapping = new HashMap<>();
            for(String className:mapping.keySet()){
                //目录
                if(!className.contains(".")){
                    continue;
                }
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(MyController.class)){
                    //controller
                    localMapping.put(className,clazz.newInstance());
                    String baseUrl = "";
                    if(clazz.isAnnotationPresent(MyRequestMapping.class)){
                        MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                        baseUrl = requestMapping.value();
                    }
                    //方法
                    Method[] methods = clazz.getMethods();
                    for(Method method:methods){
                        if(!method.isAnnotationPresent(MyRequestMapping.class)){
                            continue;
                        }
                        MyRequestMapping myRequestMapping = method.getAnnotation(MyRequestMapping.class);
                        String url = (baseUrl+"/"+myRequestMapping.value()).replaceAll("/+","/");
                        localMapping.put(url,method);
                        System.out.println("Mapped "+url+","+method);
                    }
                }else if(clazz.isAnnotationPresent(MyService.class)){
                    //service
                    MyService myService = clazz.getAnnotation(MyService.class);
                    String beanName = myService.value();
                    if("".equalsIgnoreCase(beanName)){
                        beanName = clazz.getName();
                    }

                    Object instance = clazz.newInstance();
                    localMapping.put(beanName,instance);
                    for(Class<?> i: clazz.getInterfaces()){
                        localMapping.put(i.getName(),instance);
                    }
                }else{
                    continue;
                }
            }

            mapping.putAll(localMapping);

            for(Object object:mapping.values()){
                if(object==null){
                    continue;
                }
                Class<?> clazz = object.getClass();
                if(clazz.isAnnotationPresent(MyController.class)){
                    Field[] fields = clazz.getDeclaredFields();
                    for(Field field:fields){
                        if(!field.isAnnotationPresent(MyAutowired.class)){
                            continue;
                        }
                        MyAutowired annotation = field.getAnnotation(MyAutowired.class);
                        String beanName = annotation.value();
                        if("".equalsIgnoreCase(beanName)){
                            beanName = field.getType().getName();
                        }

                        field.setAccessible(true);

                        field.set(mapping.get(clazz.getName()),mapping.get(beanName));

                    }
                }

            }
        } catch (Exception e) {
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
        System.out.println("My MVC Framework is init");
    }

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
                mapping.put(clazzName,null);
            }
        }
    }
}
