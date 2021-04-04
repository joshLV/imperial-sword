// package cn.lovike.tool.basic.framework.config;
//
// // import cn.lovike.springboot.community.common.interceptor.TokenInterceptor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// import java.util.ArrayList;
// import java.util.List;
//
// /**
//  * @author lovike
//  * @since 2020-02-16
//  */
// @Configuration
// public class MvcConfig implements WebMvcConfigurer {
//
//     private static final List<String> EXCLUDE_PATH_LIST = new ArrayList<>();
//
//     static {
//         EXCLUDE_PATH_LIST.add("/upload/**");
//         EXCLUDE_PATH_LIST.add("/sys/login");
//         EXCLUDE_PATH_LIST.add("/sys/register");
//         EXCLUDE_PATH_LIST.add("/sys/code");
//         EXCLUDE_PATH_LIST.add("/sys/username");
//         EXCLUDE_PATH_LIST.add("/sys/telephone");
//         EXCLUDE_PATH_LIST.add("/sys/posts");
//         EXCLUDE_PATH_LIST.add("/sys/post/**");
//         EXCLUDE_PATH_LIST.add("/sys/comment/**");
//         EXCLUDE_PATH_LIST.add("/sys/comments");
//     }
//
//     @Autowired
//     private TokenInterceptor tokenInterceptor;
//
//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//         // 拦截除放行路径外所有路径
//         registry.addInterceptor(tokenInterceptor)
//                 .addPathPatterns("/**")
//                 .excludePathPatterns(EXCLUDE_PATH_LIST);
//     }
//
//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         // 解决静态资源无法加载问题
//         registry.addResourceHandler("/**")
//                 .addResourceLocations("classpath:/static/");
//
//         // 解决 swagger-ui 界面 404 问题
//         registry.addResourceHandler("swagger-ui.html")
//                 .addResourceLocations("classpath:/META-INF/resources/");
//
//         // 解决 swagger-ui 新界面 404 问题
//         registry.addResourceHandler("doc.html")
//                 .addResourceLocations("classpath:/META-INF/resources/");
//
//         // 解决 swagger 的 js 无法加载问题
//         registry.addResourceHandler("/webjars/**")
//                 .addResourceLocations("classpath:/META-INF/resources/webjars/");
//     }
//
//
//     /**
//      * 解决跨域问题:存在拦截器时实现，改为 CorsConfig 类实现即可
//      *
//      * @param registry
//      */
//     // @Override
//     // public void addCorsMappings(CorsRegistry registry) {
//     //     registry.addMapping("/**")
//     //             .allowedOrigins("http://localhost:7080")
//     //             .allowedHeaders("*")
//     //             .allowedMethods("*")
//     //             .maxAge(30 * 1000);
//     // }
// }
