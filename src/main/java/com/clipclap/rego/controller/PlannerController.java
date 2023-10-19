package com.clipclap.rego.controller;


import com.clipclap.rego.model.dto.*;
import com.clipclap.rego.model.entitiy.Planner;
import com.clipclap.rego.model.entitiy.PlannerDetail;
import com.clipclap.rego.repository.DetailPlanRepository;
import com.clipclap.rego.repository.PlannerRepository;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.service.DetailPlanService;
import com.clipclap.rego.service.PlannerService;
import com.clipclap.rego.service.TouristAttractionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/plan")
public class PlannerController {

    private final TouristAttractionService touristAttractionService;
    private final TouristAttractionRepository touristAttractionRepository;
    private final ObjectMapper objectMapper;
    private final PlannerRepository plannerRepository;
    private final DetailPlanService detailPlanService;
    private final PlannerService plannerService;
    private final DetailPlanRepository detailPlanRepository;

    @GetMapping("/list")
    public String myPlanList(Model model , PlannerDTO plannerDTO ) {

        List<PlannerDTO> planList = plannerService.findByAllId();

        model.addAttribute("planList", planList);

        return "plan/planList";
    }

    /* 특정회원의 계획 리스트 --> 마이페이지에서 활용하면 될거같음
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String myPlanList(Model model, Principal principal , PlannerDTO plannerDTO ) {

        if (principal != null){
            List<PlannerDTO> dtos = plannerService.findByUserEmail(principal.getName());

            System.out.println(dtos);
            model.addAttribute("test", dtos);
        } else {
            return "redirect:/login";
        }

        return "plan/planList";
    }*/

    /* 항공권을 통하지 않고 일반 계획 생성폼 요청 */
    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String planAddForm(Model model, Principal principal,
                              PlannerDTO plannerDTO,
                              BindingResult bindingResult) {

        return "/plan/planAdd";
    }

    /* 항공권 정보를 가지고 계획 생성폼 요청 */
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String myPlanAdd(Model model, Principal principal,
                            PlannerDTO plannerDTO,
                            @ModelAttribute FlightInfo flightInfo) {

        model.addAttribute("FlightInfo", flightInfo);
        plannerDTO.setStartDate(LocalDate.parse(flightInfo.getDepartureDate()));
        plannerDTO.setEndDate(LocalDate.parse(flightInfo.getArrivalDate()));
        model.addAttribute("plannerDTO", plannerDTO);
        return "plan/planAdd";
    }

    @PostMapping("/ajaxValid")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> planAddValid(@ModelAttribute @Valid PlannerDTO plannerDTO, BindingResult bindingResult) {

        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("isValid", false);
            Map<String, Object> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            response.put("fieldErrors", errorMap); // Key를 "fieldErrors"로 바꿈
        } else {
            response.put("isValid", true);
        }

        return ResponseEntity.ok(response);
    }


    /* 계획 생성 기능 (비행정보가 있을수도 없을수도 있기 때문에 그에따른 정보 작성 필요) */
    @PostMapping("/addPlan")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> myPlanAddValid(Model model, Principal principal,
                                                  @ModelAttribute PlannerDTO plannerDTO,
                                                  FlightInfo flightInfo) {

        // 생성된 계획 번호를 리턴받아 생성된 계획 페이지로 이동
        Integer id = plannerService.save(plannerDTO);

        return ResponseEntity.ok(id);
    }

    @PostMapping("/addPlanwithfly")
    @ResponseBody
    @Transactional
    public Integer myPlanAddwithFly(@RequestBody JsonNode requestData) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode formDataNode = requestData.get("formData");
        JsonNode flightInfoNode = requestData.get("flightInfo");

        try {
            FlightInfo flightInfoDTO = objectMapper.treeToValue(flightInfoNode, FlightInfo.class);
            TravelInfoDTO travelInfoDTO = objectMapper.treeToValue(formDataNode, TravelInfoDTO.class);

            // System.out.println(flightInfoDTO.getRoutes().size());
            // 왕복 정보만 존재하기 때문에 이렇게 가능하지만 편도를 구현한다면 조건을 만들어서 구현
            RouteInfo fromHome = flightInfoDTO.getRoutes().get(0);
            RouteInfo toHome = flightInfoDTO.getRoutes().get(1);

            // Plan 생성
            PlannerDTO plannerDTO = new PlannerDTO();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            plannerDTO.setNumberOfPeople(Integer.parseInt(travelInfoDTO.getNumberOfPeople()));
            plannerDTO.setUserEmail(travelInfoDTO.getUserEmail());
            plannerDTO.setContent(travelInfoDTO.getContent());
            plannerDTO.setType(travelInfoDTO.getType());
            plannerDTO.setStartDate(LocalDate.parse(travelInfoDTO.getStartDate(), formatter));
            plannerDTO.setEndDate(LocalDate.parse(travelInfoDTO.getEndDate(), formatter));

            System.out.println(LocalDateTime.parse(flightInfoDTO.getArrivalDate() + "T" + fromHome.getDepartureTime()));
            System.out.println(LocalDateTime.parse(flightInfoDTO.getArrivalDate() + "T" + fromHome.getDepartureTime()));

            Integer id = plannerService.save(plannerDTO);

            String priceString = flightInfoDTO.getPrice().replace(",", "").trim();


            // DetailPlan에 저장하기 위해 객체 입력 (가는편)
            PlannerDetail fromHomePlan = new PlannerDetail();

            fromHomePlan.setDetailPlanId(1);
            fromHomePlan.setPlan(plannerRepository.findById(id).get());
            fromHomePlan.setContent("Go");
            fromHomePlan.setStartTime(LocalDateTime.parse(flightInfoDTO.getDepartureDate() + "T" + fromHome.getDepartureTime()));
            fromHomePlan.setEndTime(LocalDateTime.parse(flightInfoDTO.getDepartureDate() + "T" + fromHome.getArrivalTime()));
            fromHomePlan.setAllday(true);
            fromHomePlan.setTouristAttraction(touristAttractionRepository.findById(1000).get());
            fromHomePlan.setPrice(Integer.parseInt(priceString));
            fromHomePlan.setAirlineImg(fromHome.getAirlineImg());
            fromHomePlan.setAirlineName(fromHome.getAirlineName());
            fromHomePlan.setDepartureTime(fromHome.getDepartureTime());
            fromHomePlan.setDepartureAirport(fromHome.getDepartureAirport());
            fromHomePlan.setArrivalTime(fromHome.getArrivalTime());
            fromHomePlan.setArrivalAirport(fromHome.getArrivalAirport());

            detailPlanRepository.save(fromHomePlan);
            // 오는편
            PlannerDetail toHomePlan = new PlannerDetail();

            toHomePlan.setDetailPlanId(2);
            toHomePlan.setPlan(plannerRepository.findById(id).get());
            toHomePlan.setContent("Re");
            toHomePlan.setStartTime(LocalDateTime.parse(flightInfoDTO.getArrivalDate() + "T" + toHome.getDepartureTime()));
            toHomePlan.setEndTime(LocalDateTime.parse(flightInfoDTO.getArrivalDate() + "T" + toHome.getArrivalTime()));
            toHomePlan.setAllday(true);
            toHomePlan.setTouristAttraction(touristAttractionRepository.findById(1001).get());
            toHomePlan.setPrice(Integer.parseInt(priceString));
            toHomePlan.setAirlineImg(toHome.getAirlineImg());
            toHomePlan.setAirlineName(toHome.getAirlineName());
            toHomePlan.setDepartureTime(toHome.getDepartureTime());
            toHomePlan.setDepartureAirport(toHome.getDepartureAirport());
            toHomePlan.setArrivalTime(toHome.getArrivalTime());
            toHomePlan.setArrivalAirport(toHome.getArrivalAirport());

            detailPlanRepository.save(toHomePlan);

            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    @GetMapping("/detail")
    public String map(@RequestParam(required = false) Integer planId, Model model) throws JsonProcessingException {
        PlannerDTO plannerDTO = plannerService.findById(planId);
        if (plannerDTO == null){
            return "redirect:/";
        }

        List<TouristAttractionDTO> touristAttractionListAll = touristAttractionService.touristListAll();

        String listAll = objectMapper.writeValueAsString(touristAttractionListAll);

        List<DetailPlanDTO> detailList = detailPlanService.findAllByPlan(planId);

        String detailPlan = objectMapper.writeValueAsString(detailList);

        //planner 정보(수정전 value 출력용)
        String content = plannerDTO.getContent();
        String type = plannerDTO.getType();
        int numberOfPeople = plannerDTO.getNumberOfPeople();

        model.addAttribute("content",content);
        model.addAttribute("type",type);
        model.addAttribute("numberOfPeople",numberOfPeople);

        // 상세플랜 목록
        model.addAttribute("detailPlan" , detailPlan);
        // 전체 관광지 리스트
        model.addAttribute("attractionList" , listAll);
        // 도시 리스트 (검색)
        model.addAttribute("cityList" , touristAttractionRepository.findDistinctCityNames());
        // 현재 사용중인 PK 번호 최대
        model.addAttribute("detailIdMax" , detailPlanService.findMaxDetailPlanIdByPlanId(planId));
        // 이후에 정보를 받아오면 필요없을듯
        model.addAttribute("planID" , planId);
        // 플래너의 시작날짜 (이것도 굳이 필요없을 수도)
        // model.addAttribute("startDate" , plannerService.findStartTimeByPlanId(planId));
        model.addAttribute("startDate" , plannerDTO.getStartDate());

        model.addAttribute("endDate" , plannerDTO.getEndDate());

        return "plan/planDetail";
    }

    @GetMapping("/Preview")
    public String planPreview(@RequestParam(required = false) Integer planId, Model model) throws JsonProcessingException {
        PlannerDTO plannerDTO = plannerService.findById(planId);
        if (plannerDTO == null){
            return "redirect:/";
        }

        List<TouristAttractionDTO> touristAttractionListAll = touristAttractionService.touristListAll();

        String listAll = objectMapper.writeValueAsString(touristAttractionListAll);

        List<DetailPlanDTO> detailList = detailPlanService.findByPlanPlanIdOrderByStartTime(planId);

        String detailPlan = objectMapper.writeValueAsString(detailList);
        // 상세플랜 목록
        model.addAttribute("detailPlan" , detailPlan);
        model.addAttribute("previewPlan" , objectMapper.writeValueAsString(detailPlanService.findPreview(planId)));
        // 전체 관광지 리스트
        model.addAttribute("attractionList" , listAll);
        // 도시 리스트 (검색)
        model.addAttribute("cityList" , touristAttractionRepository.findDistinctCityNames());
        // 현재 사용중인 PK 번호 최대
        model.addAttribute("detailIdMax" , detailPlanService.findMaxDetailPlanIdByPlanId(planId));
        // 이후에 정보를 받아오면 필요없을듯
        model.addAttribute("planID" , planId);
        // 플래너의 시작날짜 (이것도 굳이 필요없을 수도)
        // model.addAttribute("startDate" , plannerService.findStartTimeByPlanId(planId));
        model.addAttribute("startDate" , plannerDTO.getStartDate());

        model.addAttribute("endDate" , plannerDTO.getEndDate());
        model.addAttribute("userEmail" , plannerDTO.getUserEmail());

        return "plan/planPreview";
    }

    @PostMapping("/saveImage")
    @ResponseBody
    public Map<String, Object> saveImage(@RequestBody Map<String, Object> dataToSend) {

        System.out.println("사진 저장 실행...............");
        Map<String, Object> response = new HashMap<>();
        try {
            String base64Data = ((String) dataToSend.get("imageDataURL")).replace("data:image/png;base64,", "");
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            int planId = Integer.parseInt(String.valueOf(dataToSend.get("planId")));
            String filename = "preview/" + planId + ".png"; // 저장할 이미지 파일 이름
            File imageFile = new File("src/main/resources/static/" + filename);

            String folderPath = "src/main/resources/static/preview";
            Path path = Paths.get(folderPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path); // 폴더가 없으면 생성
            }

            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(imageBytes);
            }
            response.put("success", true);
        } catch (IOException e) {
            response.put("success", false);
        }
        System.out.println("사진 저장 완료...............");
        return response;
    }


    @PostMapping("/dateEdit")
    @ResponseBody
    public String editDate(@RequestParam("start") LocalDate startDate,
                           @RequestParam("end") LocalDate endDate,
                           @RequestParam("planId") Integer planId,
                           @RequestParam("type") String type,
                           @RequestParam("content") String content,
                           @RequestParam("numberOfPeople") int numberOfPeople
    ) {
        LocalDate defaultStart = plannerRepository.findByPlanId(planId).getStartDate();

        plannerRepository.updateStartDateAndEndDate(planId, startDate, endDate);

        // 두 날짜 사이의 차이를 일 단위로 계산합니다.
        long daysBetween = ChronoUnit.DAYS.between(defaultStart, startDate);

        System.out.println("날짜의 차 : " + daysBetween);
        // detailPlan에서 planId가 일치하는 것을 찾아 날짜 변화만큼 startTime과 endTime을 모두 더하거나 빼줌.
        int updateDetailPlanCnt = detailPlanService.updateStartTimeAndEndTime(planId, daysBetween);
        System.out.println("업데이트된 detailPlan의 개수 : " + updateDetailPlanCnt);
        int updatePlanCnt = plannerService.updateContentAndTypeAndNumberOfPeople(planId, content, type, numberOfPeople);
        System.out.println("업데이트된 Planner의 개수 : " + updatePlanCnt);

        return "success";
    }

    @GetMapping("/copy/{planId}")
    public String copyPlanner(@PathVariable Integer planId, Principal principal) {
        String loggedInUserEmail = principal.getName();
        Planner newPlanner = plannerService.copyPlanner(planId, loggedInUserEmail);
        return "redirect:/plan/detail?planId=" + newPlanner.getPlanId();
    }


    @RequestMapping("/Complete")
    public String completePlanner(@RequestParam Integer planId){
        plannerService.changeIsComplete(planId);
        return "redirect:/plan/Preview?planId="+planId;
    }
}