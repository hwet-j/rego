package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.FlightInfo;
import com.clipclap.rego.model.dto.RouteInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlService {

    // 재시도횟수 //
    private static final int MAX_RETRIES = 3;

    public List<FlightInfo> getFlightInfo(String departureAirportName, String arrivalAirportName, String departureDay, String arrivalDay) throws IOException {
        String htmlLink = "https://m-flight.naver.com/flights/international/"
                +departureAirportName+"-"+arrivalAirportName+"-"+departureDay+"/"
                +arrivalAirportName+"-"+departureAirportName+"-"+arrivalDay
                +"?isDrirect=true";
        System.out.println(htmlLink);
//        Runtime.getRuntime().exec("Xvfb :99 -screen 0 1024x768x16 &");
//        System.setProperty("DISPLAY", ":99");
        System.setProperty("webdriver.chrome.driver", "/home/opc/asset/driver/chromedriver-linux64/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Headless 모드로 실행
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox"); // 노트: 보안 관련 설정

        WebDriver driver = new ChromeDriver(options);

        driver.get(htmlLink);

        List<FlightInfo> flights = new ArrayList<>();
        int retries = 0;

        while (retries < MAX_RETRIES) {
            try {
                Thread.sleep(7000); // 페이지 로딩을 위한 대기

                List<WebElement> flightDivs = driver.findElements(By.cssSelector(".concurrent_ConcurrentItemContainer__2lQVG"));
                int flightcount =0;
                for (WebElement flightDiv : flightDivs) {
                    if(flightcount++>=30){
                        break;
                    }
                    List<RouteInfo> routes = new ArrayList<>();

                    List<WebElement> airlineDivs = flightDiv.findElements(By.cssSelector(".concurrent_RoundDiffAL__22zB4, .concurrent_RoundSameAL__1Y3j3"));
                    for (WebElement airlineDiv : airlineDivs) {
                        String airlineImg = airlineDiv.findElement(By.cssSelector(".logos img")).getAttribute("src");
                        String airlineName = airlineDiv.findElement(By.cssSelector(".name")).getText();

                        List<WebElement> routeInfoDivs = airlineDiv.findElements(By.cssSelector(".route_Route__2UInh"));
                        for (WebElement routeInfoDiv : routeInfoDivs) {
                            String departureTime = routeInfoDiv.findElement(By.cssSelector(".route_time__-2Z1T")).getText();
                            String departureAirport = routeInfoDiv.findElement(By.cssSelector(".route_code__3WUFO")).getText();
                            String arrivalTime = routeInfoDiv.findElements(By.cssSelector(".route_time__-2Z1T")).get(1).getText();
                            String arrivalAirport = routeInfoDiv.findElements(By.cssSelector(".route_code__3WUFO")).get(1).getText();
                            String duration = routeInfoDiv.findElement(By.cssSelector(".route_info__1RhUH")).getText();
                            System.out.println(departureTime+departureAirport+arrivalTime+arrivalAirport+duration);
                            routes.add(new RouteInfo(airlineImg, airlineName, departureTime, departureAirport, arrivalTime, arrivalAirport, duration));
                        }
                    }

                    String price = flightDiv.findElement(By.cssSelector(".item_num__3R0Vz")).getText();
                    flights.add(new FlightInfo(routes, price));
                }
                break; // 웹 요소 찾기에 성공했으면 종료
            } catch (Exception e) {
                retries++;
                if (retries == MAX_RETRIES) {
                    throw new RuntimeException("Failed to access the web elements after multiple retries");
                }
            }
        }

        driver.quit();
        return flights;
    }
}
