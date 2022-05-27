package cz.cvut.fit.tjv.shareddesks.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class SecurityController {

  @GetMapping("/")
  public String home() {
    if (SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null
        && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        && !(SecurityContextHolder.getContext().getAuthentication()
            instanceof AnonymousAuthenticationToken)) {
      log.info(
          "Is authenticated: {}",
          SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
      return "HomeTemplateAfterLogin";
    }
    return "HomeTemplate";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/login/success")
  public String successfulLogin() {
    return "HomeTemplateAfterLogin";
  }

  @GetMapping("/logout")
  public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
      auth.setAuthenticated(false);
    }
    return "redirect:/login?logout";
  }
}
