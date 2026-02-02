package com.team7.StemHub.controller.web;

import com.team7.StemHub.dto.view.HomePageView;
import com.team7.StemHub.facade.HomePageFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final HomePageFacade homePageFacade;

    @GetMapping("")
    public String home(Model model, @RequestParam(defaultValue = "1") int page) {
        HomePageView viewData = homePageFacade.prepareHomePageData(page);
        model.addAttribute("newestDocumentsPage", viewData.getNewestDocumentsPage());
        model.addAttribute("topDocuments", viewData.getTopDocuments());
        model.addAttribute("users", viewData.getTopUsers());
        model.addAttribute("categories", viewData.getCategories());
        return "home/home";
    }

    @GetMapping("/category")
    public String category(@RequestParam String category, Model model, @RequestParam(defaultValue = "1") int page) {
        HomePageView viewData = homePageFacade.prepareCategoryPageData(category, page);
        model.addAttribute("topDocuments", viewData.getTopDocuments());
        model.addAttribute("newestDocumentsPage", viewData.getNewestDocumentsPage());
        model.addAttribute("users", viewData.getTopUsers());
        model.addAttribute("categories", viewData.getCategories());
        model.addAttribute("category", viewData.getCategory());
        return "home/home";
    }

    @GetMapping("/about")
    public String about() {
        return "home/about";
    }

    @GetMapping("/search/{keyword}")
    public String search(Model model) {
        return "home/search";
    }
}