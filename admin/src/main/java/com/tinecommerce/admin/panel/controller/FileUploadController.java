package com.tinecommerce.admin.panel.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tinecommerce.admin.panel.repository.AdminMenuGroupRepository;
import com.tinecommerce.admin.panel.repository.AdminMenuItemRepository;
import com.tinecommerce.admin.panel.repository.DynamicEntityDao;
import com.tinecommerce.core.catalog.model.MediaAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.tinecommerce.core.catalog.service.StorageService;
import java.io.IOException;
import java.util.stream.Collectors;


@RequestMapping("/entities/mediaAsset")
@Controller
public class FileUploadController
{
    @Autowired
    private StorageService storageService;
    @Autowired
    private AdminMenuGroupRepository adminMenuGroupRepository;

    @Autowired
    private AdminMenuItemRepository adminMenuItemRepository;

    @Autowired
    private DynamicEntityDao dynamicEntityDao;

    @GetMapping("/add")
    public String addAssetForm(Model model ) {

        model.addAttribute("menuItems", adminMenuGroupRepository.findAll());
        model.addAttribute("entityName", "media assets");

        return "mediaForm";
    }

    @PostMapping("/add")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file, name);
        MediaAsset mediaAsset = new MediaAsset("/entities/mediaAsset/" + name, file.getName());
        mediaAsset.setDescription("file description");
        dynamicEntityDao.save(mediaAsset);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/entities/mediaAsset";
    }
}