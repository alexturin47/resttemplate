package resttemplate;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import resttemplate.entity.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class resttemplateApp {

        private static List<String> cookies;
        private static final String URL = "http://91.241.64.178:7081/api/users";

    public static void main(String[] args) {
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> forEntity = template.getForEntity(URL, String.class);
        ResponseEntity<List<User>> response = template
                .exchange(URL, HttpMethod.GET, null,  new ParameterizedTypeReference<List<User>>() {
                });
        cookies = Objects.requireNonNull(forEntity.getHeaders().get("Set-Cookie"));
        cookies.forEach(System.out::println);

        List<User> users = response.getBody();

        assert users != null;
        users.forEach(u -> System.out.printf("%d %s %s %d\n",u.getId(), u.getName(), u.getLastName(), u.getAge()));

        User user = new User( 3L, "James", "Brown", (byte) 23);

       // RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie",cookies.stream().collect(Collectors.joining(";")));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<User> entity = new HttpEntity<User>(user, headers);
        String code = template.exchange(URL, HttpMethod.POST, entity, String.class).getBody();

        System.out.println(code);

        user.setName("Thomas");
        user.setLastName("Shelby");
        entity = new HttpEntity<User>(user, headers);
        code += template.exchange(URL, HttpMethod.PUT, entity, String.class).getBody();

        System.out.println(code);

        entity = new HttpEntity<User>(headers);
        code += template.exchange(URL+"/"+user.getId(), HttpMethod.DELETE, entity, String.class).getBody();

        System.out.println(code);
    }
}
