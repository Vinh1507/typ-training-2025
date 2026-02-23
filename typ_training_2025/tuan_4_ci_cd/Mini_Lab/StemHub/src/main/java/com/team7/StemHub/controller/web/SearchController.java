package com.team7.StemHub.controller.web;

import com.team7.StemHub.dto.view.SearchView;
import com.team7.StemHub.facade.SearchFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller đã được tái cấu trúc, trở nên "mỏng" và "sạch".
 * Chỉ còn inject SearchFacade.
 */
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchFacade searchFacade;

    @RequestMapping("/search")
    public String search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int userPage,
            @RequestParam(defaultValue = "1") int documentPage,
            Model model
    ) {
        SearchView viewData = searchFacade.performSearch(keyword, userPage, documentPage);
        model.addAttribute("courses", viewData.getCourses());
        model.addAttribute("documents", viewData.getDocuments());
        model.addAttribute("users", viewData.getUsers());
        model.addAttribute("keyword", viewData.getKeyword());
        return "home/search";
    }
}