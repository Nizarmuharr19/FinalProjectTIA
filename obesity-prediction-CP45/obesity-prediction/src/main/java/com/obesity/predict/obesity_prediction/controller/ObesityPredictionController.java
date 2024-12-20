package com.obesity.predict.obesity_prediction.controller;

import com.obesity.predict.obesity_prediction.dto.ObesityData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
@RequestMapping("/predict")
public class ObesityPredictionController {

    // Endpoint untuk menampilkan form di browser (Thymeleaf)
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("obesityData", new ObesityData());
        return "predict";
    }

    // Endpoint untuk menghandle form  dari browser
    @PostMapping("/submit")
    public String submitFormPrediction(@ModelAttribute ObesityData obesityData, Model model) {
        String predictionResult = runPythonScript(obesityData);
        model.addAttribute("predictionResult", predictionResult);
        return "predict";
    }

    // Endpoint untuk menerima data JSON dari Postman
    @PostMapping(value = "/api", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> submitJsonPrediction(@RequestBody ObesityData obesityData) {
        String predictionResult = runPythonScript(obesityData);
        return ResponseEntity.ok().body(predictionResult);
    }

    // Method untuk menjalankan script Python
    private String runPythonScript(ObesityData data) {
        try {
            String[] command = {
                    "D:\\obesity-prediction\\obesity-prediction\\src\\main\\resources\\model\\venv\\Scripts\\python.exe",
                    "D:\\obesity-prediction\\obesity-prediction\\src\\main\\resources\\model\\venv\\ObesityPredict_cmd2.py",
                    data.getGender(),
                    String.valueOf(data.getAge()),
                    String.valueOf(data.getHeight()),
                    String.valueOf(data.getWeight()),
                    data.getFamilyHistoryWithOverweight(),
                    data.getFavc(),
                    String.valueOf(data.getFcvc()),
                    String.valueOf(data.getNcp()),
                    data.getCaec(),
                    data.getSmoke(),
                    String.valueOf(data.getCh2o()),
                    data.getScc(),
                    String.valueOf(data.getFaf()),
                    String.valueOf(data.getTue()),
                    data.getCalc(),
                    data.getMtrans()
            };

            // Menjalankan script Python
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            return (exitCode == 0) ? result.toString() : "Error dalam prediksi.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }
}

