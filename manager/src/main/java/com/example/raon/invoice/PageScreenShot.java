package com.example.raon.invoice;

import org.aspectj.util.FileUtil;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class PageScreenShot {

	public static void main(String[] args) {
        // 크롬 드라이버 경로 설정
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // 크롬 브라우저 옵션 설정 (headless 모드)
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        // WebDriver 객체 생성
        WebDriver driver = new ChromeDriver(options);

        try {
            // 웹 페이지 접속
            driver.get("http://example.com");

            // 스크린샷 캡쳐
            File sc = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
 
            // 이미지 파일로 저장 (예: "screenshot.png")
            File destinationFile = new File("path/to/screenshot.png");
            FileUtil.copyFile(sc, destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
    }
}