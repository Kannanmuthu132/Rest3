package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UseMedicalController {

	@Autowired
	RestTemplate restTemp;

	@GetMapping("/getAllWithGst")
	public List<MedicalEntity> getAllWithGst() {
		String Medicalurl = "http://localhost:8080/readinfo";
		String GSTurl = "http://localhost:8081/getByHsn/";
		ResponseEntity<List<MedicalEntity>> respose1 = restTemp.exchange(Medicalurl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<MedicalEntity>>() {
				});
		List<MedicalEntity> medicalproducts = respose1.getBody();
		medicalproducts.forEach(x -> {
			Integer hsn = x.getHsn();
			ResponseEntity<Integer> finalprice = restTemp.exchange(GSTurl + hsn, HttpMethod.GET, null, Integer.class);
			Integer finalprice1 = finalprice.getBody();
			x.setPrice(x.getPrice() + (x.getPrice() * finalprice1 / 100));
		});
		return medicalproducts;
	}
}