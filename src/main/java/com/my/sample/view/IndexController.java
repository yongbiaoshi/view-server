package com.my.sample.view;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class IndexController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("ex")
    public String ex() {
        throw new RuntimeException("测试异常返回");
    }

    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
        HttpStatus statusCode = getStatus(request);
        mv.setStatus(statusCode);
        if (HttpStatus.NOT_FOUND.equals(statusCode)) {
            mv.setViewName("index");
        } else {
            mv.setViewName("error");
        }
        Map<String, Object> errorAttributes = new LinkedHashMap<>();

        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("status", statusCode);
        errorAttributes.put("error", request.getAttribute("javax.servlet.error.exception"));
        errorAttributes.put("message", request.getAttribute("javax.servlet.error.message"));
        mv.addAllObjects(errorAttributes);
        return mv;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
