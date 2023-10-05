package com.clipclap.rego.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlService {
    public List<FlightInfo> getFlightInfo(String departureAirportName, String arrivalAirportName, String departureDay, String arrivalDay){
        // 검색할 링크
        /*String htmlLink = "https://m-flight.naver.com/flights/international/SEL-CDG-20231010/CDG-SEL-20231025?adult=2&isDirect=true&fareType=N%252";*/
        String htmlLink = "https://m-flight.naver.com/flights/international/"
                +departureAirportName+"-"+arrivalAirportName+"-"+departureDay+"/"
                +arrivalAirportName+"-"+departureAirportName+"-"+arrivalDay;
        System.out.println(htmlLink);
        // 크롬 드라이버 경로 설정
        System.setProperty("webdriver.chrome.driver", "C:\\coding\\driver\\chromedriver.exe");
        //운항들을 담을 객체를 생성
        List<FlightInfo> flights = new ArrayList<>();
        
        // ChromeOptions 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");  // 원하시면 User-Agent도 변경 가능

        // WebDriver에 ChromeOptions 적용
        WebDriver driver = new ChromeDriver(options);
        driver.get(htmlLink);

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        boolean progressVisible = true;

        while (progressVisible) {
            try {
                shortWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div[class*='progress']")));
            } catch (TimeoutException e) {
                progressVisible = false;
            }
        }

        List<WebElement> flightDivs = driver.findElements(By.cssSelector(".concurrent_ConcurrentItemContainer__2lQVG"));
        for (WebElement flightDiv : flightDivs) {
            List<RouteInfo> routes = new ArrayList<>();

            // 항공사 이름, 이미지, 시간, 공항 정보를 가져옴
            List<WebElement> airlineDivs = flightDiv.findElements(By.cssSelector(".concurrent_RoundDiffAL__22zB4, .concurrent_RoundSameAL__1Y3j3"));

            for (WebElement airlineDiv : airlineDivs) {
                String airlineImg = airlineDiv.findElement(By.cssSelector(".logos img")).getAttribute("src");
                String airlineName = airlineDiv.findElement(By.cssSelector(".name")).getText();

                // 노선 정보들을 가져옴
                List<WebElement> routeInfoDivs = airlineDiv.findElements(By.cssSelector(".route_Route__2UInh"));
                for (WebElement routeInfoDiv : routeInfoDivs) {
                    String departureTime = routeInfoDiv.findElement(By.cssSelector(".route_time__-2Z1T")).getText();
                    String departureAirport = routeInfoDiv.findElement(By.cssSelector(".route_code__3WUFO")).getText();
                    String arrivalTime = routeInfoDiv.findElements(By.cssSelector(".route_time__-2Z1T")).get(1).getText();
                    String arrivalAirport = routeInfoDiv.findElements(By.cssSelector(".route_code__3WUFO")).get(1).getText();
                    String duration = routeInfoDiv.findElement(By.cssSelector(".route_info__1RhUH")).getText();

                    routes.add(new RouteInfo(airlineImg, airlineName, departureTime, departureAirport, arrivalTime, arrivalAirport, duration));
                }
            }

            String price = flightDiv.findElement(By.cssSelector(".item_num__3R0Vz")).getText();
            flights.add(new FlightInfo(routes, price));
        }

        driver.quit();

        return flights;
    }
}
