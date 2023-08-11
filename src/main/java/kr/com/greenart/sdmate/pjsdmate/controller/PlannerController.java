package kr.com.greenart.sdmate.pjsdmate.controller;

import kr.com.greenart.sdmate.pjsdmate.domain.Planner;
import kr.com.greenart.sdmate.pjsdmate.domain.PlannermainpageCard;
import kr.com.greenart.sdmate.pjsdmate.service.PlannerMainPageService;
import kr.com.greenart.sdmate.pjsdmate.service.PlannerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;

@Controller
@RequestMapping("/planner")
public class PlannerController {

    private final PlannerService plannerService;
    private final PlannerMainPageService plannerMainPageService;

    public PlannerController(PlannerService plannerService, PlannerMainPageService plannerMainPageService) {
        this.plannerService = plannerService;
        this.plannerMainPageService = plannerMainPageService;
    }

    @GetMapping("/main")
    public String mainplanner(Model model, HttpSession session) {
        Planner planner = (Planner) session.getAttribute("planner");
        List<PlannermainpageCard> card = plannerMainPageService.returnPlannerMainCard(planner.getPlannerNo());
        model.addAttribute("card", card);

        return "mainplanner";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @PostMapping("/login")
    public String Login(@RequestParam String id, @RequestParam String pw, @RequestParam String userstat, Model
            model, HttpSession session, HttpServletResponse response, HttpServletRequest request){
        if(userstat.equals("planner,member")||userstat.equals("member")) {
            try {
                request.setAttribute("id", id);
                request.setAttribute("pw", pw);
                request.getRequestDispatcher("/member/login").forward(request, response);
                return "forward:/member/login";
            } catch (ServletException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            Planner planner = plannerService.login(id, pw);
            if (planner != null) {

                if ((model.getAttribute("AutoLogin") != null)) {
                    Cookie cookie = new Cookie("id", planner.getId());
                    response.addCookie(cookie);
                }
                session.setAttribute("planner", planner);

                session.setAttribute("userPk", planner.getPlannerNo());

                return "redirect:/planner/main";

            } else {
                model.addAttribute("error", "로그인 실패");

            }
        }

        return "redirect:./login?userstat=planner";
    }
    @GetMapping("/join")
    public String join() {
        return "planner_join";
    }

    @PostMapping("/join")
    public String Join(@Valid @ModelAttribute("planner") Planner planner, BindingResult result, Model model){
        if(result.hasErrors()){
            return "planner_join";
        }
        // 이메일 중복 검사
        if (plannerService.isEmailDuplicated(planner.getEmail())){
            result.rejectValue("email", "Duplicated" , "이미 사용중인 이메일 입니다.");
        }
        // 아이디 중복 검사
        if (plannerService.isIdDuplicated(planner.getId())){
            result.rejectValue("id", "Duplicated" , "이미 사용중인 아이디 입니다.");
        }
        // 사업자 번호 중복 검사
        if (plannerService.isBusinessNoDuplicated(planner.getBusiness_no())){
            result.rejectValue("business_no", "Duplicated" , "이미 사용중인 사업자 번호 입니다.");
        }
        // 전화번호 중복 검사
        if (plannerService.isPhoneNoDuplicated(planner.getPhonenum())){
            result.rejectValue("phonenum", "Duplicated" , "이미 사용중인 전화번호 입니다.");
        }
        String json = (String)model.getAttribute("planner");

        plannerService.join(json);
        return "login";
    }
}


