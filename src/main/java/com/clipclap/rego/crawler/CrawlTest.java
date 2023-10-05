package com.clipclap.rego.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CrawlTest {
    @Autowired
    CrawlService crawlService;

    @GetMapping("/flightSearch")
    public String flightSearch(){
        return "crawl/flightSearch";
    }

    @GetMapping("/flightResult")
    public String noArgRoundCrawl(Model model
                                , @RequestParam String departureAirport
                                , @RequestParam String arrivalAirport
                                , @RequestParam String departureDate
                                , @RequestParam String arrivalDate){
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

        String formattedDepartureDate = LocalDate.parse(departureDate, inputFormat).format(outputFormat);
        String formattedArrivalDate = LocalDate.parse(arrivalDate, inputFormat).format(outputFormat);

        List<FlightInfo> flights = crawlService.getFlightInfo(departureAirport, arrivalAirport, formattedDepartureDate, formattedArrivalDate);
        model.addAttribute("flights", flights);
        return "crawl/roundCrawl";
    }


    @GetMapping("/crawl")
    public ModelAndView noArgCrawl(ModelAndView modelAndView){
        // 검색할 링크
        String htmlLink = "https://m-flight.naver.com/flights/international/SEL-KIX-20231010?adult=1&isDirect=true&fareType=N";
        // 크롬 드라이버 경로 설정
        System.setProperty("webdriver.chrome.driver", "C:\\coding\\driver\\chromedriver.exe");

        // ChromeOptions 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");  // 원하시면 User-Agent도 변경 가능

        // WebDriver에 ChromeOptions 적용
        WebDriver driver = new ChromeDriver(options);
        driver.get(htmlLink);

        List<airLineDTO> airlineList = new ArrayList<>();
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<WebElement> airlineElements = driver.findElements(By.cssSelector(".indivisual_IndivisualItem__3co62.result"));
        for (WebElement element : airlineElements) {
            String airline = element.findElement(By.cssSelector(".airline .name")).getText();

            List<WebElement> routeElements = element.findElements(By.cssSelector(".route_Route__2UInh .route_airport__3VT7M"));
            String departureTime = routeElements.get(0).findElement(By.cssSelector(".route_time__-2Z1T")).getText();
            String departureAirport = routeElements.get(0).findElement(By.cssSelector(".route_code__3WUFO")).getText();
            String arrivalTime = routeElements.get(1).findElement(By.cssSelector(".route_time__-2Z1T")).getText();
            String arrivalAirport = routeElements.get(1).findElement(By.cssSelector(".route_code__3WUFO")).getText();
            String duration = element.findElement(By.cssSelector(".route_info__1RhUH")).getText();

            airLineDTO dto = new airLineDTO(airline, departureTime, departureAirport, arrivalTime, arrivalAirport, duration);
            airlineList.add(dto);
        }

        driver.quit();

        modelAndView.addObject("airlines", airlineList);
        modelAndView.setViewName("crawl/crawl");
        return modelAndView;
    }

}
