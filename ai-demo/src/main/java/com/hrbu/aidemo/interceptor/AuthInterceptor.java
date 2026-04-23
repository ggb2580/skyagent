//package com.hrbu.aidemo.interceptor;
//
//import com.hrbu.aidemo.util.JWTUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//@Component
//public class AuthInterceptor implements HandlerInterceptor {
//    @Autowired
//    private JWTUtil jwtUtil;
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //从请求头获取token
//        String token = request.getHeader("Authorization");
//
//        if (token == null || !token.startsWith("Bearer ")){
//            response.setStatus(401);
//            response.getWriter().write("Unauthorized: No token provided");
//            return false;
//        }
//
//        token = token.substring(7);
//        if (!jwtUtil.validateToken(token)){
//            response.setStatus(401);
//            response.getWriter().write("Unauthorized: Invalid token");
//            return false;
//        }
//        // 鉴权通过，放行调用 LangChain4j
//        return true;
//    }
//}
