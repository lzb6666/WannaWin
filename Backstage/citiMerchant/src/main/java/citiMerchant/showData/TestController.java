package citiMerchant.showData;

import citiMerchant.vo.Merchant_coupon_record;
import citiMerchant.vo.Record;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class TestController {

    @Autowired
    TestService testService;

    @Autowired
    Gson gson;

    @RequestMapping("/showData")
    public ModelAndView getInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String merchantID = session.getAttribute("merchantID").toString();
        ModelAndView mv = new ModelAndView();

        //
        List<Long> points = testService.show_order_points_chronology("1", "2018");
        List<Long> timestamp = testService.getMonthTimeStamp("2018");
        List<Long> points_exchange = testService.show_points_exchange_chronology("1", "2018");
        List<List<Merchant_coupon_record>> merchant_coupon_record = testService.show_Merchant_coupon_record("1", "2018");

        //set attribute
        String points_json = gson.toJson(points);
        session.setAttribute("points_json", points_json);

        String timeStamp_json = gson.toJson(timestamp);
        session.setAttribute("timeStamp_json", timeStamp_json);

        String points_exchange_json = gson.toJson(points_exchange);
        session.setAttribute("points_exchange_json", points_exchange_json);

        String merchant_coupon_record_json = gson.toJson(merchant_coupon_record);
        session.setAttribute("merchant_coupon_record_json", merchant_coupon_record_json);

        System.out.println(merchant_coupon_record_json);

        mv.setViewName("/showData/showData");
        return mv;
    }


    @RequestMapping("/test")
    public void getRecord() {
        System.out.println("\n------------------------------------------------------------\n");
        Record record1 = testService.getCouponRecord("123", 7);
        System.out.println("totalPoints of coupons in 7 days of merchantID\"123\": " + record1.getTotalPoints());

        System.out.println("\n------------------------------------------------------------\n");
        Record record2 = testService.getOrderRecord("00002", 7);
        System.out.println("totalPoints of orders in 7 days of merchantID\"00002\": " + record2.getTotalPoints());

        System.out.println("\n------------------------------------------------------------\n");
        System.out.println("\ntotalPoints of orders in 2018 of merchantID\"00002\":\n");
        List<Long> points = testService.show_order_points_chronology("00002", "2018");
        for (Long l : points) {
            System.out.print(l + "\t");
        }
        System.out.println("\n------------------------------------------------------------\n");

    }


}
