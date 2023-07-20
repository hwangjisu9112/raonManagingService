package com.example.raon.invoice;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PageScreenShot {
	
	private final InvoiceService invoiceService;

	@PostMapping("/invoice/view/{id}")
	public String saveInvoiceImage(@RequestBody String image, @PathVariable Long id, Model model) {
		List<Invoice> invoices = invoiceService.getInvoicebyId(id);
		Invoice invoice = invoices.isEmpty() ? null : invoices.get(0);
		model.addAttribute("invoice", invoice);
		  
		// Base64로 인코딩된 데이터 URL에서 이미지 데이터 부분을 추출합니다.
		String imageData = image.split(",")[1];
		
		// 바탕화면 경로 가져오기
		String desktopPath = System.getProperty("user.home") + "/Desktop";
		String filePath = desktopPath + "/captured_image.png";
		
		try {
			// Base64 데이터를 디코딩하여 이미지 파일로 저장합니다.
			byte[] decodedImage = Base64.getDecoder().decode(imageData);
			Path path = Paths.get(filePath);
			Files.write(path, decodedImage);

			return "redirect:/invoice/view/" + id;

		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
	}
}