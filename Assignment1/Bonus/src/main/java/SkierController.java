import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SkierController {
    private static final String[] SKIER_POST_BODY = new String[]{"time", "liftID"};
    private final Gson gson = new Gson();

    private boolean isUrlValid(String[] urlPath) {
        if (urlPath.length == 8) {
            return urlPath[0].chars().allMatch(Character::isDigit) && urlPath[2].equals("seasons") &&
                    urlPath[3].chars().allMatch(Character::isDigit) && urlPath[4].equals("days") &&
                    urlPath[5].chars().allMatch(Character::isDigit) && urlPath[6].equals("skiers") &&
                    urlPath[7].chars().allMatch(Character::isDigit) && Integer.parseInt(urlPath[5]) >= 1 &&
                    Integer.parseInt(urlPath[5]) <= 365;
        }
        return false;
    }

    @PostMapping("/skiers")
    public ResponseEntity<String> skierEndpoint(HttpServletRequest request, @RequestBody String requestBody) {
        JsonObject body = gson.fromJson(requestBody, JsonObject.class);

        // Check if URL is valid
        String[] parts = request.getPathInfo().split("/");
        if (!isUrlValid(parts)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Message\": \"URL is not correct!\"}");
        }

        // Check if body params are present
        for (String param : SKIER_POST_BODY) {
            if (body == null || body.get(param) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Message\": \"Body param is missing!\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"Message\": \"URL is valid!\"}");
    }
}