import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunObesityPredict {
    public static void main(String[] args) {
        try {
            String[] command = {
                "python", 
                "ObesityPredict_cmd.py", 
                "Male",                // Gender
                "25",                  // Age
                "175",                 // Height
                "70",                  // Weight
                "yes",                 // family_history_with_overweight
                "yes",                 // FAVC
                "2",                   // FCVC
                "3",                   // NCP
                "Sometimes",           // CAEC
                "no",                  // SMOKE
                "2",                   // CH2O
                "no",                  // SCC
                "1",                   // FAF
                "2",                   // TUE
                "Sometimes",           // CALC
                "Public_Transportation" // MTRANS
            };

           
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); 
            Process process = processBuilder.start();

   
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
