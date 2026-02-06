package com.team7.StemHub.controller.web;

import com.team7.StemHub.dto.request.DocumentRequest;
import com.team7.StemHub.dto.view.DocumentDetailView;
import com.team7.StemHub.dto.view.UploadFormView;
import com.team7.StemHub.exception.FileUploadException;
import com.team7.StemHub.facade.DocumentFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/document")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {
    private final DocumentFacade documentFacade;

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        UploadFormView viewData = documentFacade.prepareUploadFormData();
        model.addAttribute("categories", viewData.getCategories());
        model.addAttribute("userId", viewData.getUserId());
        model.addAttribute("courses", viewData.getCourses());
        return "home/upload";
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleDocumentUpload(@ModelAttribute DocumentRequest documentRequest,
                                       RedirectAttributes redirectAttributes) {
        try {
            documentFacade.handleDocumentUpload(documentRequest);
            redirectAttributes.addFlashAttribute("message", "Tải tài liệu lên thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/";

        } catch (FileUploadException e) {
            log.warn("Failed document upload: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/document/upload";
        } catch (Exception e) {
            log.error("Error uploading document: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("message", "Lỗi hệ thống khi tải tài liệu lên.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/document/upload";
        }
    }

    @GetMapping("/detail/{documentId}")
    public String viewDocumentDetail(@PathVariable UUID documentId, Model model) {
        DocumentDetailView viewData = documentFacade.getDocumentDetailView(documentId);
        model.addAttribute("document", viewData.getDocument());
        model.addAttribute("relatedDocument", viewData.getRelatedDocument());
        model.addAttribute("lastComment", viewData.getLastComment());
        model.addAttribute("currentUserId", viewData.getCurrentUserId());
        model.addAttribute("liked", viewData.isLiked());
        return "home/detail";
    }
}