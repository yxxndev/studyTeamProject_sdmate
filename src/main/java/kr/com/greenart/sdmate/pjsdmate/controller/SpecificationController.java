package kr.com.greenart.sdmate.pjsdmate.controller;

import kr.com.greenart.sdmate.pjsdmate.domain.*;
import kr.com.greenart.sdmate.pjsdmate.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class SpecificationController {

    private final SpecificationService specificationService;
    private final RequirementService requirementService;

    private final MySpecificationService mySpecificationService;

    private final PlannerService plannerService;

    private final MemberService memberService;
    public SpecificationController(SpecificationService specificationService, RequirementService requirementService, MySpecificationService mySpecificationService, PlannerService plannerService, MemberService memberService) {
        this.specificationService = specificationService;
        this.requirementService = requirementService;
        this.mySpecificationService = mySpecificationService;
        this.plannerService = plannerService;
        this.memberService = memberService;
    }

    @GetMapping("/viewSpecification")
    public String viewSpecification(@RequestParam String specification, Model model){

        Specification objSpecification =  specificationService.getSpecificationByNo(Integer.parseInt(specification));
        int sum = objSpecification.calculateSumExceptSpecNoAndState();
        Requirement requirement = requirementService.getRequirementByNo(objSpecification.getRequirement_no());

        SendRequirement sendRequirement = requirementService.setttingRequirement(requirement);

        Planner planner = plannerService.findBySepcificationInPackage(objSpecification.getSpecificationNo());

        System.out.println(requirement);
        model.addAttribute("sum",sum);
        model.addAttribute("planner", planner);
        model.addAttribute("specification",objSpecification);
        model.addAttribute("requirement",sendRequirement);
        return "estimate_member";
    }
    @GetMapping("/plannerInfo")
    public String plannerInfo(){
    return "plannerInfo";
    }
    @GetMapping("/userInfo")
    public  String viewUser(){
        return "userInfo";
    }


    @GetMapping("/viewMySpecification")
    public String Myspecification(@RequestParam String specificationNo, String requirementNo, Model model){

        Specification specification = mySpecificationService.returnSpecification(Integer.parseInt(specificationNo));
        Requirement requirement = mySpecificationService.returnRequirement(Integer.parseInt(requirementNo));
        Member member = mySpecificationService.returnMember(Integer.parseInt(requirementNo));
        model.addAttribute("specification", specification);
        model.addAttribute("requirement", requirement);
        model.addAttribute("Member", member);
        System.out.println("specificationNo : " + specificationNo);
        System.out.println("requirementNo : " + requirementNo);
        System.out.println("Member : " + member);
        return "estimate_planner_check";
    }
    @PostMapping("/saveSpecification")
    public ResponseEntity<String> saveSpecification(@RequestBody Specification specification, HttpSession session){
        Planner planner = (Planner) session.getAttribute("planner");
        Member member = memberService.getRequirement(specification.getRequirement_no());
        specificationService.save(specification,planner,member);

        return ResponseEntity.ok("데이터가 성공적으로 저장되었습니다");
    }

}
