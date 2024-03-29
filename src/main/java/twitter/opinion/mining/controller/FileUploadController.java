package twitter.opinion.mining.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import twitter.opinion.mining.exception.StorageFileNotFoundException;
import twitter.opinion.mining.exception.UserIdNotFoundException;
import twitter.opinion.mining.model.User;
import twitter.opinion.mining.service.UserService;
import twitter.opinion.mining.service.upload.StorageService;
import twitter.opinion.mining.util.Util;

import java.nio.file.Path;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("files")
public class FileUploadController {

    public static final Logger log= LoggerFactory.getLogger(FileUploadController.class);

    private final UserService userService;
    private final StorageService storageService;

    @Autowired
    public FileUploadController(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    /*@GetMapping("/")
    public String listUploadedFiles(Model model)throws IOException{
        model.addAttribute("files",storageService
            .loadAll().map(path-> MvcUriComponentsBuilder
                                    .fromMethodName(FileUploadController.class,"servFile",path.getFileName().toString()).build().toString())
            .collect(Collectors.toList()));
        return "uploadForm";
    }*/

    @GetMapping("/icon/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file=storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping("/icon/upload")
    public String handleFileUpload(Principal principal, @RequestParam("icon") MultipartFile file,
                                   RedirectAttributes redirectAttributes){

        String[] res=file.getOriginalFilename().split("\\.");
        String suffix=res[res.length-1];
        if(!isImageExtension(suffix)){
            Set<String> errors=new HashSet<>();
            errors.add("only image files can be uploaded.");
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:/update";
        }

        String filename=storageService.store(file);

        User user = Util.getLoginuserFromPrincipal(principal);
        Path path=storageService.load(filename);

        log.info("path.filename.tostring: "+path.getFileName().toString());
        user.setIconPath( getPathStrFromFilename(path.getFileName().toString()) );

        try{
            userService.update(user);
        }catch (UserIdNotFoundException e){
            Set<String> errors=new HashSet<>();
            errors.add(e.getMessage());
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:/update";
        }catch (Exception e){
            Set<String> errors=new HashSet<>();
            errors.add("unexpected error occured. try again.");
            log.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:/update";
        }

        Util.updateAuthenticate(principal,user);

        return "redirect:/";
    }

    public String getPathStrFromFilename(String filename){
        return MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,"serveFile",filename).build().toString();
    }

    public boolean isImageExtension(String extension){
        for (String s:Util.imageExtensions){
            if(s.equals(extension)){
                return true;
            }
        }
        return false;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc){
        return ResponseEntity.notFound().build();
    }

}
