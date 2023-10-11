package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.FlightInfo;
import com.clipclap.rego.model.dto.RouteInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrawlService {

    // 재시도횟수 //
    private static final int MAX_RETRIES = 3;

   /* public List<FlightInfo> getFlightInfo(String departureAirportName, String arrivalAirportName, String departureDay, String arrivalDay){
        // 검색할 링크
        *//*String htmlLink = "https://m-flight.naver.com/flights/international/SEL-CDG-20231010/CDG-SEL-20231025?adult=2&isDirect=true&fareType=N%252";*//*
        String htmlLink = "https://m-flight.naver.com/flights/international/"
                +departureAirportName+"-"+arrivalAirportName+"-"+departureDay+"/"
                +arrivalAirportName+"-"+departureAirportName+"-"+arrivalDay
                +"?isDrirect=true";
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


        int retries = 0;

        try {
            Thread.sleep(5000);  // 10초(10000밀리초) 동안 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (retries < MAX_RETRIES) {
            try {
                List<WebElement> flightDivs = driver.findElements(By.cssSelector(".concurrent_ConcurrentItemContainer__2lQVG"));
                for (WebElement flightDiv : flightDivs) {
                    List<RouteInfo> routes = new ArrayList<>();
                    // ... (생략: 기존의 for 문 내부 코드)
                }

                break;  // for문이 에러 없이 완료되면 반복문 탈출
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                retries++;
                if (retries == MAX_RETRIES) {
                    throw new RuntimeException("Failed to access the web elements after multiple retries");
                }

                // 잠시 대기 후 재시도
                try {
                    Thread.sleep(2000);  // 2초 대기
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
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
                    System.out.println(departureTime+departureAirport+arrivalTime+arrivalAirport+duration);
                    routes.add(new RouteInfo(airlineImg, airlineName, departureTime, departureAirport, arrivalTime, arrivalAirport, duration));
                }
            }

            String price = flightDiv.findElement(By.cssSelector(".item_num__3R0Vz")).getText();
            flights.add(new FlightInfo(routes, price));
        }

        driver.quit();

        return flights;
    }*/

    public List<FlightInfo> getFlightInfo(String departureAirportName, String arrivalAirportName, String departureDay, String arrivalDay){
        String htmlLink = "https://m-flight.naver.com/flights/international/"
                +departureAirportName+"-"+arrivalAirportName+"-"+departureDay+"/"
                +arrivalAirportName+"-"+departureAirportName+"-"+arrivalDay
                +"?isDrirect=true";

        System.setProperty("webdriver.chrome.driver", "C:\\coding\\driver\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

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
