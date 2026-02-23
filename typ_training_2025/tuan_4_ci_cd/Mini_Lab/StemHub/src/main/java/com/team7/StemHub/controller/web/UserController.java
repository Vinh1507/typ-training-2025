package com.team7.StemHub.controller.web;

import com.team7.StemHub.dto.view.UserFavoritesView;
import com.team7.StemHub.dto.view.UserProfileView;
import com.team7.StemHub.exception.NotAuthenticatedException;
import com.team7.StemHub.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserFacade userFacade;

    @GetMapping("/profile")
    public String viewProfile(@RequestParam UUID userId, @RequestParam(defaultValue = "1") int page, Model model) {
        UserProfileView viewData = userFacade.prepareUserProfileView(userId, page);
        model.addAttribute("user", viewData.getUser());
        model.addAttribute("documents", viewData.getDocuments());
        model.addAttribute("currentUserId", viewData.getCurrentUserId());
        model.addAttribute("likedIds", viewData.getLikedIds());
        return "home/profile";
    }

    @GetMapping("/favorites")
    public String favoriteDocuments(@RequestParam(defaultValue = "1") int page, Model model) {
        try {
            UserFavoritesView viewData = userFacade.prepareUserFavoritesView(page);
            model.addAttribute("user", viewData.getUser());
            model.addAttribute("documents", viewData.getDocuments());
            return "home/favorite";
        } catch (NotAuthenticatedException e) {
            log.warn("Unauthenticated user tried to access favorites. Redirecting to login.");
            return "redirect:/auth/login";
        }
    }
}