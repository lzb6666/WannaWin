package citiMerchant.login;

import citiMerchant.showData.Prepare_info;
import citiMerchant.showData.Session_store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;


/**
 * Created by zhong on 2018/7/11 19:51
 *
 * @author 彭璇
 */
@Controller

public class LoginController {
    @Autowired
    private LoginService loginService;


    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login/login");
        return mv;
    }

    @RequestMapping("/loginSubmit")
    public ModelAndView loginSubmit(String merchantID, String password) {
        ModelAndView mv = new ModelAndView();
        ArrayList<Integer> nums = loginService.getNums(merchantID, password);
        if (nums == null) {
            mv.setViewName("login/fail");
            return mv;
        } else {
            mv.addObject("nums", nums);
            mv.addObject("merchant", loginService.getMerchant(merchantID));
            mv.setViewName("starter");
            //添加session
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("merchantID", merchantID);
            session.setAttribute("password", password);

            //prepare for statistics information to display
            Session_store.addSession(merchantID, session);
            Prepare_info prepare = new Prepare_info();
            prepare.set(merchantID);
            new Thread(prepare).start();

            return mv;
        }

    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        ModelAndView mv = new ModelAndView();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().invalidate();
        mv.setViewName("login/login");
        return mv;
    }

    @RequestMapping("/starter")
    public ModelAndView starter() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String merchantID = (String) session.getAttribute("merchantID");
        String password = (String) session.getAttribute("password");
        return loginSubmit(merchantID, password);
    }


}
